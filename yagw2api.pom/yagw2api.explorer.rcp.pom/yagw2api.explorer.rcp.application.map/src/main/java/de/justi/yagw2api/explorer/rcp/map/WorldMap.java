package de.justi.yagw2api.explorer.rcp.map;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * yagw2api.explorer.rcp.application.map
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.annotation.Nullable;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import de.justi.yagw2api.explorer.rcp.Activator;
import de.justi.yagw2api.explorer.rcp.map.ZoomManager.ZoomChangedCallback;

public class WorldMap extends ViewPart implements ZoomChangedCallback {

	// CONST
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldMap.class);
	private static final int SECTOR_SIZE = 256;
	public static final String ID = "de.justi.yagw2api.explorer.rcp.map.worldmap"; //$NON-NLS-1$
	public static final ThreadFactory DAEMON_THREAD_FACTORY = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("MAP-UPDATER-DAEMON-%d")
			.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(final Thread t, final Throwable e) {
					LOGGER.error("Uncaught exception in {}", t, e);
				}
			}).build();

	// FIELDS
	private final ExecutorService mapUpdaterService;
	@Nullable
	private ScrolledComposite scrolling = null;
	@Nullable
	private ZoomManager zoomManager = null;
	@Nullable
	private Text txtZoom = null;

	// CONSTRUCTOR
	public WorldMap() {
		this.mapUpdaterService = Executors.newSingleThreadExecutor(DAEMON_THREAD_FACTORY);
	}

	// METHODS
	private void initializeMapAsync(final int continentId, final int floor, final int zoom, final int vSectors, final int hSectors) {
		this.mapUpdaterService.submit(new Runnable() {

			@Override
			public void run() {
				final Image cache = new Image(Display.getCurrent(), hSectors * SECTOR_SIZE, vSectors * SECTOR_SIZE);
				final GC gc = new GC(cache);
				for (int x = 0; x <= hSectors; x++) {
					for (int y = 0; y <= vSectors; y++) {
						final Optional<Path> source = Activator.getDefault().getMapTileService().getMapTile(continentId, floor, zoom, x, y);
						if (source.isPresent()) {
							final Image image = SWTResourceManager.getImage(source.get().toAbsolutePath().toString());
							gc.drawImage(image, 0, 0, SECTOR_SIZE, SECTOR_SIZE, x * SECTOR_SIZE, y * SECTOR_SIZE, SECTOR_SIZE, SECTOR_SIZE);
						}
					}
				}
				gc.dispose();
				final Rectangle cacheBounds = cache.getBounds();
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						final Canvas mapCanvas = new Canvas(WorldMap.this.scrolling, SWT.BORDER);
						mapCanvas.addPaintListener(new PaintListener() {

							@Override
							public void paintControl(final PaintEvent e) {
								e.gc.drawImage(cache, 0, 0, cacheBounds.width, cacheBounds.height, 0, 0, cacheBounds.width, cacheBounds.height);
							}
						});
						WorldMap.this.scrolling.setContent(mapCanvas);
						WorldMap.this.scrolling.setMinSize(cacheBounds.width, cacheBounds.height);
						WorldMap.this.scrolling.redraw();
						WorldMap.this.scrolling.layout();
					}
				});
			}
		});
	}

	/**
	 * Create contents of the view part.
	 *
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {

		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(5, false));

		CLabel lblZoom = new CLabel(parent, SWT.NONE);
		lblZoom.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblZoom.setText("Zoom");

		this.txtZoom = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);
		this.txtZoom.setText("100%");
		this.txtZoom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Button btnZoomIn = new Button(parent, SWT.FLAT | SWT.CENTER);
		GridData gd_btnZoomIn = new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1);
		gd_btnZoomIn.widthHint = 25;
		btnZoomIn.setLayoutData(gd_btnZoomIn);
		btnZoomIn.setText("+");

		Button btnZoomOut = new Button(parent, SWT.FLAT | SWT.CENTER);
		GridData gd_btnZoomOut = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnZoomOut.widthHint = 25;
		btnZoomOut.setLayoutData(gd_btnZoomOut);
		btnZoomOut.setText("-");
		this.scrolling = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		this.scrolling.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
		this.scrolling.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		this.scrolling.setExpandHorizontal(true);
		this.scrolling.setExpandVertical(true);

		CLabel lblFloor = new CLabel(parent, SWT.NONE);
		lblFloor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblFloor.setText("Floor");

		Spinner spnFloor = new Spinner(parent, SWT.BORDER | SWT.READ_ONLY);
		spnFloor.setMaximum(10);
		spnFloor.setMinimum(-10);
		spnFloor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));

		CLabel lblContinent = new CLabel(parent, SWT.NONE);
		lblContinent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblContinent.setText("Continent");

		Combo cmbContinent = new Combo(parent, SWT.READ_ONLY);
		GridData gd_cmbContinent = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_cmbContinent.widthHint = 125;
		cmbContinent.setLayoutData(gd_cmbContinent);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		this.createActions();
		this.initializeToolBar();
		this.initializeMenu();

		this.zoomManager = new ZoomManager(btnZoomIn, btnZoomOut, this);
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = this.getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = this.getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void onZoomLevelChanged(final int oldZoom, final int newZoom) {
		this.initializeMapAsync(1, 1, newZoom, 2, 2);
		this.txtZoom.setText(newZoom * 100 + "%");
	}

}
