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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.justi.yagw2api.explorer.rcp.map.ContinentManager.ContinentChangedCallback;
import de.justi.yagw2api.explorer.rcp.map.FloorManager.FloorChangedCallback;
import de.justi.yagw2api.explorer.rcp.map.ZoomManager.ZoomChangedCallback;

public class WorldMap extends ViewPart implements ZoomChangedCallback, FloorChangedCallback, ContinentChangedCallback {

	// CONST
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldMap.class);
	public static final String ID = "de.justi.yagw2api.explorer.rcp.map.worldmap"; //$NON-NLS-1$

	// FIELDS
	private final ZoomManager zoomManager;
	private final FloorManager floorManager;
	private final ContinentManager continentManager;
	@Nullable
	private Text txtZoom = null;
	@Nullable
	private MapWidget map = null;
	@Nullable
	private Button btnZoomOut = null;
	@Nullable
	private Button btnZoomIn = null;
	private Spinner spnFloor;
	private Combo cmbContinent;

	// CONSTRUCTOR
	public WorldMap() {
		this.zoomManager = new ZoomManager(this);
		this.floorManager = new FloorManager(this);
		this.continentManager = new ContinentManager(this);
	}

	// METHODS

	/**
	 * Create contents of the view part.
	 *
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {

		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(2, false));

		Composite cmpBasicControls = new Composite(parent, SWT.NONE);
		cmpBasicControls.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		cmpBasicControls.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmpBasicControls.setLayout(new GridLayout(4, false));

		CLabel lblZoom = new CLabel(cmpBasicControls, SWT.NONE);
		lblZoom.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblZoom.setText("Zoom");

		this.txtZoom = new Text(cmpBasicControls, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);

		this.btnZoomIn = new Button(cmpBasicControls, SWT.FLAT | SWT.CENTER);
		this.btnZoomIn.setText("+");

		this.btnZoomOut = new Button(cmpBasicControls, SWT.FLAT | SWT.CENTER);
		this.btnZoomOut.setSize(25, 25);
		this.btnZoomOut.setText("-");

		CLabel lblFloor = new CLabel(cmpBasicControls, SWT.NONE);
		lblFloor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblFloor.setText("Floor");

		this.spnFloor = new Spinner(cmpBasicControls, SWT.BORDER | SWT.READ_ONLY);
		this.spnFloor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		this.spnFloor.setMaximum(10);
		this.spnFloor.setMinimum(-10);
		this.spnFloor.setSelection(1);

		CLabel lblContinent = new CLabel(cmpBasicControls, SWT.NONE);
		lblContinent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblContinent.setText("Continent");

		this.cmbContinent = new Combo(cmpBasicControls, SWT.READ_ONLY);
		this.cmbContinent.setItems(new String[] { "Tyria", "Mists" });
		this.cmbContinent.select(0);
		this.cmbContinent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		this.map = new MapWidget(parent);
		GridLayout gridLayout = (GridLayout) this.map.getLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		this.map.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		this.map.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		Composite cmpNavigation = new Composite(parent, SWT.NONE);
		cmpNavigation.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmpNavigation.setLayout(new GridLayout(3, false));
		cmpNavigation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		new Label(cmpNavigation, SWT.NONE);

		Button btnUp = new Button(cmpNavigation, SWT.CENTER);
		btnUp.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnUp.setText("UP");
		new Label(cmpNavigation, SWT.NONE);

		Button btnLeft = new Button(cmpNavigation, SWT.CENTER);
		btnLeft.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnLeft.setText("LEFT");

		Button btnReset = new Button(cmpNavigation, SWT.CENTER);
		btnReset.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnReset.setText("RESET");

		Button btnRight = new Button(cmpNavigation, SWT.CENTER);
		btnRight.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnRight.setText("RIGHT");
		new Label(cmpNavigation, SWT.NONE);

		Button btnDown = new Button(cmpNavigation, SWT.CENTER);
		btnDown.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnDown.setText("DOWN");
		new Label(cmpNavigation, SWT.NONE);
		this.createActions();
		this.initializeToolBar();
		this.initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		this.wireUpZoomManager();
		this.wireUpFloorManager();
		this.wireUpContinentManager();
	}

	private void wireUpContinentManager() {
		this.cmbContinent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				WorldMap.this.continentManager.updateValue(WorldMap.this.cmbContinent.getSelectionIndex() + 1);
			}
		});
		this.continentManager.reset();
	}

	private void wireUpFloorManager() {
		this.spnFloor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				WorldMap.this.floorManager.updateValue(WorldMap.this.spnFloor.getSelection());
			}
		});
		this.floorManager.reset();
	}

	private void wireUpZoomManager() {
		this.btnZoomOut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				WorldMap.this.zoomManager.decrement();
			}
		});
		this.btnZoomIn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				WorldMap.this.zoomManager.increment();
			}
		});
		this.zoomManager.reset();
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
	public void onZoomChanged(final int oldZoom, final int newZoom) {
		checkNotNull(this.map, "missing map");
		checkNotNull(this.txtZoom, "missing txtZoom");
		this.map.setZoomAndUpdate(newZoom);
		this.txtZoom.setText(newZoom * 100 + "%");
	}

	@Override
	public void onFloorChanged(final int oldFloor, final int newFloor) {
		checkNotNull(this.map, "missing map");
		this.map.setFloorAndUpdate(newFloor);
	}

	@Override
	public void onContinentChanged(final int oldContinent, final int newContinent) {
		checkNotNull(this.map, "missing map");
		this.map.setContinentAndUpdate(newContinent);
	}

}
