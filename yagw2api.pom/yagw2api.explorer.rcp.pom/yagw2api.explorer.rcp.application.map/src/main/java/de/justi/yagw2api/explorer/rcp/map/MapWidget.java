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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.FluentIterable;
import com.google.common.eventbus.Subscribe;

import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.map.MapWrapper;
import de.justi.yagw2api.wrapper.map.domain.Continent;
import de.justi.yagw2api.wrapper.map.domain.MapFloor;
import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagw2api.wrapper.map.domain.NoSuchMapTileException;
import de.justi.yagw2api.wrapper.map.event.MapTileImageFailedToLoadEvent;
import de.justi.yagw2api.wrapper.map.event.MapTileImageLoadedSuccessfullyEvent;
import de.justi.yagw2api.wrapper.map.event.MapTileImageNotAvailableEvent;
import de.justi.yagwapi.common.Tuple2;
import de.justi.yagwapi.common.Tuples;

public final class MapWidget extends Composite implements PaintListener {
	// CONSTS
	private static final int SOURCE_TILE_SIZE = 256;
	private static final int DEFAULT_TARGET_TILE_SIZE = 64;
	private static final Logger LOGGER = LoggerFactory.getLogger(MapWidget.class);
	// FIELDS
	private final MapWrapper mapWrapper = YAGW2APIWrapper.INSTANCE.getMapWrapper();
	private final Display display;

	private final AtomicBoolean scrollingActive = new AtomicBoolean(false);
	private final ScrolledComposite scrolling;
	private final Canvas mapCanvas;

	private int continentId = 1;
	private int floor = 0;
	private int tileSize = DEFAULT_TARGET_TILE_SIZE;
	private int zoom = 1;

	private Tuple2<Integer, Integer> scrollableAreaSize = Tuples.of(0, 0);
	private int lastNotifiedScrolledX = 0;
	private int lastNotifiedScrolledY = 0;

	// CONSTRUCTOR
	public MapWidget(final Composite parent) {
		super(parent, SWT.BORDER);
		this.setLayout(new GridLayout(1, false));

		this.scrolling = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		this.scrolling.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		this.scrolling.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.scrolling.setExpandHorizontal(true);
		this.scrolling.setExpandVertical(true);
		MapWidget.this.scrolling.setMinSize(35000, 35000);

		final SelectionAdapter scrollingControl = new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (e.detail != SWT.DRAG) {
					if (MapWidget.this.scrollingActive.getAndSet(false)) {
						LOGGER.trace("Stopped dragging...");
						MapWidget.this.updateMap();
					}
					final ScrollBar barX = MapWidget.this.scrolling.getHorizontalBar();
					final ScrollBar barY = MapWidget.this.scrolling.getVerticalBar();
					final double scrolledX = ((double) barX.getSelection()) / ((double) (Math.max(1, barX.getMaximum() - barX.getThumb())));
					final double scrolledY = ((double) barY.getSelection()) / ((double) (Math.max(1, barY.getMaximum() - barY.getThumb())));
					final double x = MapWidget.this.scrolling.getMinWidth() * scrolledX;
					final double y = MapWidget.this.scrolling.getMinHeight() * scrolledY;
					final int xIndex = (int) (x / MapWidget.this.tileSize);
					final int yIndex = (int) (y / MapWidget.this.tileSize);
					LOGGER.trace("scrolled=({}%/{}%) -> position=({}/{}) -> index=({}/{})", (scrolledX * 100), (scrolledY * 100), x, y, xIndex, yIndex);
					if (xIndex != MapWidget.this.lastNotifiedScrolledX || yIndex != MapWidget.this.lastNotifiedScrolledY) {
						MapWidget.this.lastNotifiedScrolledX = xIndex;
						MapWidget.this.lastNotifiedScrolledY = yIndex;
						MapWidget.this.updateMap();
					}
				} else {
					LOGGER.trace("Dragging...");
					MapWidget.this.scrollingActive.set(true);
				}
			}
		};
		this.scrolling.getHorizontalBar().addSelectionListener(scrollingControl);
		this.scrolling.getVerticalBar().addSelectionListener(scrollingControl);
		this.scrolling.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(final ControlEvent e) {
				final Control control = (Control) e.getSource();
				MapWidget.this.scrollableAreaSize = Tuples.of(control.getSize().x, control.getSize().y);
				LOGGER.trace("Resized {} to {}", control, MapWidget.this.scrollableAreaSize);
			}
		});

		this.mapCanvas = new Canvas(this.scrolling, SWT.NONE);
		this.mapCanvas.addPaintListener(this);
		this.scrolling.setContent(this.mapCanvas);

		this.display = this.getShell().getDisplay();
		this.mapWrapper.getChannel().register(this);
	}

	// METHODS
	public void setFloorAndUpdate(final int floor) {
		this.floor = floor;
		this.updateMap();
	}

	public void setZoomAndUpdate(final int zoom) {
		this.zoom = zoom / 100;
		double tileSizeFactor = 1d + ((zoom % 100) / 100d);
		this.tileSize = (int) (tileSizeFactor * DEFAULT_TARGET_TILE_SIZE);
		this.updateMap();
	}

	public void setContinentAndUpdate(final int continentId) {
		this.continentId = continentId;
		this.updateMap();
	}

	@Subscribe
	public void onMapTileImageNotAvailableEvent(final MapTileImageNotAvailableEvent e) {
		this.updateMapForTile(e.getMapTile());
	}

	@Subscribe
	public void onMapTileImageFailedToLoadEvent(final MapTileImageFailedToLoadEvent e) {
		this.updateMapForTile(e.getMapTile());
	}

	@Subscribe
	public void onMapTileImageLoadedSuccessfully(final MapTileImageLoadedSuccessfullyEvent e) {
		this.updateMapForTile(e.getMapTile());
	}

	private void updateMapForTile(final MapTile tile) {
		checkNotNull(tile, "missing tile");
		LOGGER.info("Update map for tile={}", tile);
		this.updateMapRect(Tuples.of(tile.getPosition().v1() * this.tileSize, tile.getPosition().v2() * this.tileSize), Tuples.of(this.tileSize, this.tileSize));
	}

	private void updateMap() {
		LOGGER.trace("Update complete map");
		this.display.asyncExec(() -> {
			this.mapCanvas.redraw();
		});
	}

	private synchronized void updateMapRect(final Tuple2<Integer, Integer> position, final Tuple2<Integer, Integer> size) {
		checkNotNull(position, "missing position");
		checkNotNull(size, "missing size");
		this.display.asyncExec(() -> {
			LOGGER.info("Update map position={}, width={}, height={} ", position, size.v1(), size.v2());
			this.mapCanvas.redraw(position.v1(), position.v2(), size.v1(), size.v2(), false);
		});
	}

	private final Image loadImage(final Path path) {
		checkNotNull(path, "missing path");
		try (final InputStream stream = Files.newInputStream(path)) {
			final ImageData data = new ImageData(stream);
			if (data.transparentPixel > 0) {
				return new Image(this.display, data, data.getTransparencyMask());
			} else {
				return new Image(this.display, data);
			}
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	@Override
	public void paintControl(final PaintEvent e) {
		final Optional<Continent> continent = FluentIterable.from(this.mapWrapper.getContinents()).filter((c) -> c.getId().equals(String.valueOf(this.continentId))).first();
		if (continent.isPresent()) {
			LOGGER.trace("RepaintEvent: ({}/{}) - ({}/{})", e.x, e.y, e.width, e.height);
			final MapFloor tiles = continent.get().getFloor(MapWidget.this.floor);
			final Tuple2<Integer, Integer> mapFloorBounds = tiles.getTileIndexDimension(this.zoom);
			final int minX = Math.max(0, (e.x / this.tileSize));
			final int maxX = Math.min(Math.max(minX, ((e.x + e.width) / this.tileSize)) + (((e.x + e.width) % this.tileSize > 0) ? 1 : 0), mapFloorBounds.v1());
			final int minY = Math.max(0, (e.y / this.tileSize));
			final int maxY = Math.min(Math.max(minY, ((e.y + e.height) / this.tileSize)) + (((e.y + e.height) % this.tileSize > 0) ? 1 : 0), mapFloorBounds.v2());
			if (!this.scrollingActive.get()) {
				LOGGER.trace(" > draw tiles ({}/{})-({}/{})", minX, minY, maxX - 1, maxY - 1);
				for (int x = minX; x < maxX; x++) {
					for (int y = minY; y < maxY; y++) {
						try {
							final MapTile tile = tiles.getTile(x, y, MapWidget.this.zoom);
							final Image img = this.loadImage(tile.getImagePath());
							try {
								e.gc.drawImage(img, 0, 0, SOURCE_TILE_SIZE, SOURCE_TILE_SIZE, x * this.tileSize, y * this.tileSize, this.tileSize, this.tileSize);
								// e.gc.setForeground(SWTResourceManager.getColor(255, 0, 0));
								// e.gc.drawText(x + "/" + y, x * this.tileSize, y * this.tileSize);
								// e.gc.drawRectangle(x * this.tileSize, y * this.tileSize, this.tileSize, this.tileSize);
							} finally {
								img.dispose();
							}
						} catch (NoSuchMapTileException t) {
							Throwables.propagate(t);
						}
					}
				}
			} else {
				LOGGER.trace(" > skip drawing");
			}
		} else {
			e.gc.drawText("Unknown continent with id=" + this.continentId, 0, 0);
		}
	}
}
