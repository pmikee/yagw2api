package de.justi.yagw2api.explorer.rcp.map;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Explorer-RCP-Application World-Map
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadFactory;

import javax.annotation.Nullable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import de.justi.yagw2api.explorer.rcp.Activator;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;

public final class MapWidget extends Composite {
	// CONSTS
	private static final int SECTOR_SIZE = 256;
	private static final Logger LOGGER = LoggerFactory.getLogger(MapWidget.class);
	private static final ThreadFactory DAEMON_THREAD_FACTORY = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("MAP-UPDATER-DAEMON-%d")
			.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(final Thread t, final Throwable e) {
					LOGGER.error("Uncaught exception in {}", t, e);
				}
			}).build();

	// FIELDS
	private final ExecutorService mapUpdaterService;
	private final Display display;
	@Nullable
	private ScrolledComposite scrolling = null;

	private int continentId = 1;
	private int floor = 1;
	private int zoom = 1;
	private int vSectors = 10;
	private int hSectors = 10;

	// CONSTRUCTOR
	public MapWidget(final Composite parent) {
		super(parent, SWT.NONE);
		this.setLayout(new GridLayout(1, false));
		this.scrolling = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		this.scrolling.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		this.scrolling.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.scrolling.setExpandHorizontal(true);
		this.scrolling.setExpandVertical(true);
		this.display = this.getShell().getDisplay();
		this.mapUpdaterService = Executors.newSingleThreadExecutor(DAEMON_THREAD_FACTORY);
	}

	// METHODS
	public void setFloorAndUpdate(final int floor) {
		this.floor = floor;
		this.updateMapAsync();
	}

	public void setZoomAndUpdate(final int zoom) {
		this.zoom = zoom;
		this.updateMapAsync();
	}

	public void setContinentAndUpdate(final int continentId) {
		this.continentId = continentId;
		this.updateMapAsync();
	}

	private static final class QueryForTileImagesTask extends RecursiveTask<Table<Integer, Integer, Image>> {
		private static final long serialVersionUID = 7630590156145085484L;
		private static final int MAX_TILES_AT_ONCE = 4;
		private final int continentId;
		private final int floor;
		private final int zoom;
		private final int xOffset;
		private final int width;
		private final int yOffset;
		private final int height;

		public QueryForTileImagesTask(final int continentId, final int floor, final int zoom, final int xOffset, final int width, final int yOffset, final int height) {
			this.continentId = continentId;
			this.floor = floor;
			this.zoom = zoom;

			this.xOffset = xOffset;
			this.width = width;
			this.yOffset = yOffset;
			this.height = height;
		}

		@Override
		protected Table<Integer, Integer, Image> compute() {
			final ImmutableTable.Builder<Integer, Integer, Image> resultBuilder = ImmutableTable.builder();
			if (this.width * this.height <= MAX_TILES_AT_ONCE) {
				// compute directly

				final ImmutableTable.Builder<Integer, Integer, Future<Optional<Path>>> requestBuilder = ImmutableTable.builder();
				for (int x = this.xOffset; x < this.xOffset + this.width; x++) {
					for (int y = this.yOffset; y < this.yOffset + this.height; y++) {
						requestBuilder.put(y, x, Activator.getDefault().getMapTileService().getMapTileAsync(this.continentId, this.floor, this.zoom, x, y));
					}
				}
				final Table<Integer, Integer, Future<Optional<Path>>> requests = requestBuilder.build();
				for (int x : requests.columnKeySet()) {
					for (int y : requests.rowKeySet()) {
						try {
							Optional<Path> source = requests.get(y, x).get();
							if (source.isPresent()) {
								final Image image = SWTResourceManager.getImage(source.get().toAbsolutePath().toString());
								resultBuilder.put(y, x, image);
							} else {
								LOGGER.warn("Missing tile for continentId={}, floor={}, zoom={}, x={} and y={}", this.continentId, this.floor, this.zoom, x, y);
							}
						} catch (InterruptedException | ExecutionException e) {
							throw new Error(e);
						}
					}
				}
			} else {
				// fork

				// calculate required pivots
				final int leftWidth = this.width / 2;
				final int leftXOffset = this.xOffset;
				final int leftHeight = this.height / 2;
				final int leftYOffset = this.yOffset;
				final int rightXOffset = leftXOffset + leftWidth;
				final int rightWidth = this.width - leftWidth;
				final int rightYOffset = leftYOffset + leftHeight;
				final int rightHeight = this.height - leftHeight;

				// chunk the task into for quadrants
				final QueryForTileImagesTask topLeft = new QueryForTileImagesTask(this.continentId, this.floor, this.zoom, leftXOffset, leftWidth, leftYOffset, leftHeight);
				final QueryForTileImagesTask topRight = new QueryForTileImagesTask(this.continentId, this.floor, this.zoom, rightXOffset, rightWidth, leftYOffset, leftHeight);
				final QueryForTileImagesTask bottomLeft = new QueryForTileImagesTask(this.continentId, this.floor, this.zoom, leftXOffset, leftWidth, rightYOffset, rightHeight);
				final QueryForTileImagesTask bottomRight = new QueryForTileImagesTask(this.continentId, this.floor, this.zoom, rightXOffset, rightWidth, rightYOffset, rightHeight);

				invokeAll(topLeft, topRight, bottomLeft, bottomRight);
				try {
					// merge all results together
					resultBuilder.putAll(topLeft.get());
					resultBuilder.putAll(topRight.get());
					resultBuilder.putAll(bottomLeft.get());
					resultBuilder.putAll(bottomRight.get());
				} catch (InterruptedException | ExecutionException e) {
					throw new Error(e);
				}
			}
			return resultBuilder.build();
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("x", this.xOffset).add("w", this.width).add("y", this.yOffset).add("h", this.height).toString();
		}
	}

	private final Table<Integer, Integer, Image> queryForTileImages(final int xOffset, final int width, final int yOffset, final int height) {
		return YAGW2APIWrapper.INSTANCE.getForkJoinPool().invoke(new QueryForTileImagesTask(this.continentId, this.floor, this.zoom, xOffset, width, yOffset, height));
	}

	private synchronized void updateMapAsync() {
		this.mapUpdaterService.execute(() -> {
			final ScrolledComposite scrolling = this.scrolling;
			final int hSectors = this.hSectors;
			final int vSectors = this.vSectors;
			final Table<Integer, Integer, Image> images = this.queryForTileImages(0, hSectors, 0, vSectors);
			MapWidget.this.display.syncExec(new Runnable() {
				@Override
				public void run() {
					final Canvas mapCanvas = new Canvas(scrolling, SWT.BORDER);
					mapCanvas.addPaintListener(new PaintListener() {

						@Override
						public void paintControl(final PaintEvent e) {
							for (int x : images.columnKeySet()) {
								for (int y : images.rowKeySet()) {
									if (images.get(y, x) != null) {
										e.gc.drawImage(images.get(y, x), 0, 0, SECTOR_SIZE, SECTOR_SIZE, x * SECTOR_SIZE, y * SECTOR_SIZE, SECTOR_SIZE, SECTOR_SIZE);
									}
									e.gc.setForeground(SWTResourceManager.getColor(255, 0, 0));
									e.gc.drawText(x + "/" + y + "^" + MapWidget.this.floor, x * SECTOR_SIZE, y * SECTOR_SIZE);
									e.gc.drawRectangle(x * SECTOR_SIZE, y * SECTOR_SIZE, SECTOR_SIZE, SECTOR_SIZE);
								}
							}
						}
					});

					scrolling.setMinSize(images.columnKeySet().size() * SECTOR_SIZE, images.rowKeySet().size() * SECTOR_SIZE);
					scrolling.setContent(mapCanvas);
					scrolling.redraw();
					scrolling.layout();
				}
			});
		});
	}
}
