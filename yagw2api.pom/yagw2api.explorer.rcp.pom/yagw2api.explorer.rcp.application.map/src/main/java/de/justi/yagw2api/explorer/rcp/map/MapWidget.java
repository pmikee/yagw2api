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

import java.util.concurrent.ThreadFactory;

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

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.map.MapWrapper;
import de.justi.yagw2api.wrapper.map.domain.Continent;
import de.justi.yagw2api.wrapper.map.domain.MapFloorTiles;
import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagw2api.wrapper.map.domain.NoSuchMapTileException;

public final class MapWidget extends Composite implements PaintListener {
	// CONSTS
	private static final int SECTOR_SIZE = 256;
	private static final Logger LOGGER = LoggerFactory.getLogger(MapWidget.class);
	private static final ThreadFactory DAEMON_THREAD_FACTORY = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("MAP-UPDATER-DAEMON-%d")
			.setUncaughtExceptionHandler((t, e) -> LOGGER.error("Uncaught exception in {}", t, e)).build();

	// FIELDS
	private final MapWrapper mapWrapper = YAGW2APIWrapper.INSTANCE.getMapWrapper();
	private final Display display;

	private final ScrolledComposite scrolling;
	private final Canvas mapCanvas;

	private int continentId = 1;
	private int floor = 1;
	private int zoom = 1;
	private int vSectors = 10;
	private int hSectors = 10;

	// CONSTRUCTOR
	public MapWidget(final Composite parent) {
		super(parent, SWT.BORDER);
		this.setLayout(new GridLayout(1, false));

		this.scrolling = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		this.scrolling.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		this.scrolling.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.scrolling.setExpandHorizontal(true);
		this.scrolling.setExpandVertical(true);

		this.mapCanvas = new Canvas(this.scrolling, SWT.NONE);
		this.mapCanvas.addPaintListener(this);
		this.scrolling.setContent(this.mapCanvas);

		this.display = this.getShell().getDisplay();
	}

	// METHODS
	public void setFloorAndUpdate(final int floor) {
		this.floor = floor;
		this.updateMap();
	}

	public void setZoomAndUpdate(final int zoom) {
		this.zoom = zoom;
		this.updateMap();
	}

	public void setContinentAndUpdate(final int continentId) {
		this.continentId = continentId;
		this.updateMap();
	}

	private synchronized void updateMap() {
		this.display.syncExec(() -> {
			this.mapCanvas.redraw();
		});
	}

	@Override
	public void paintControl(final PaintEvent e) {
		final Optional<Continent> continent = FluentIterable.from(this.mapWrapper.getContinents()).filter((c) -> c.getId().equals(String.valueOf(this.continentId))).first();

		if (continent.isPresent()) {
			final MapFloorTiles tiles = continent.get().getMap().getFloorTiles(continent.get().getMap().getFloors().get(MapWidget.this.floor));
			for (int x = 0; x < MapWidget.this.hSectors; x++) {
				for (int y = 0; y < MapWidget.this.vSectors; y++) {
					MapTile tile;
					try {
						tile = tiles.getTile(x, y, MapWidget.this.zoom);
						final Image img = SWTResourceManager.getImage(tile.getImagePath().toString());
						e.gc.drawImage(img, 0, 0, SECTOR_SIZE, SECTOR_SIZE, x * SECTOR_SIZE, y * SECTOR_SIZE, SECTOR_SIZE, SECTOR_SIZE);
						e.gc.setForeground(SWTResourceManager.getColor(255, 0, 0));
						e.gc.drawText(x + "/" + y + ": " + tile.getImagePath().getFileName().toString(), x * SECTOR_SIZE, y * SECTOR_SIZE);
						e.gc.drawRectangle(x * SECTOR_SIZE, y * SECTOR_SIZE, SECTOR_SIZE, SECTOR_SIZE);
					} catch (NoSuchMapTileException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		} else {
			e.gc.drawText("Unknown continent with id=" + this.continentId, 0, 0);
		}
	}
}
