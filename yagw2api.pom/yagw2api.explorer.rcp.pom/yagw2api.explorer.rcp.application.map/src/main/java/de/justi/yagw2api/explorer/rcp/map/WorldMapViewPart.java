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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

import de.justi.yagw2api.explorer.rcp.swt.AggregatingSelectionProvider;
import de.justi.yagw2api.explorer.rcp.swt.TypeSafeViewerLabelProvider;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.map.MapWrapper;
import de.justi.yagw2api.wrapper.map.domain.Continent;
import de.justi.yagw2api.wrapper.map.domain.ContinentFloor;

public class WorldMapViewPart extends ViewPart implements ISelectionListener {
	private static final int ZOOM_DELTA = 10;

	private static class FloorContentProvider implements IStructuredContentProvider {
		// FIELDS

		// CONSTRUCTOR
		public FloorContentProvider() {
		}

		// METHODS
		@Override
		public ContinentFloor[] getElements(final Object inputElement) {
			checkNotNull(inputElement, "missing inputElement");
			checkArgument(inputElement instanceof Continent, "invalid inputElement: %s", inputElement);
			final Continent continent = (Continent) inputElement;
			return Iterables.toArray(continent.getFloors(), ContinentFloor.class);
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		}
	}

	private static class ContinentContentProvider implements IStructuredContentProvider {
		// FIELDS

		// CONSTRUCTOR
		public ContinentContentProvider() {
		}

		// METHODS
		@Override
		public Continent[] getElements(final Object inputElement) {
			checkNotNull(inputElement, "missing inputElement");
			checkArgument(inputElement instanceof MapWrapper, "invalid inputElement: %s", inputElement);
			final MapWrapper wrapper = (MapWrapper) inputElement;
			return wrapper.getContinents().toArray(new Continent[0]);
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		}
	}

	// CONST
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldMapViewPart.class);
	public static final String ID = "de.justi.yagw2api.explorer.rcp.map.worldmap"; //$NON-NLS-1$

	// FIELDS
	private final MapWrapper wrapper = YAGW2APIWrapper.INSTANCE.getMapWrapper();
	private final AggregatingSelectionProvider selectionProvider;
	@Nullable
	private Text txtZoom = null;
	@Nullable
	private MapWidget map = null;
	@Nullable
	private Button btnZoomOut = null;
	@Nullable
	private Button btnZoomIn = null;

	private final FloorContentProvider floorContentProvider;
	private final ContinentContentProvider continentContentProvier;
	private ComboViewer cmbFloorViewer;
	private ComboViewer cmbContinentViewer;

	private int zoom = 100;

	// CONSTRUCTOR
	public WorldMapViewPart() {
		this.floorContentProvider = new FloorContentProvider();
		this.continentContentProvier = new ContinentContentProvider();
		this.selectionProvider = new AggregatingSelectionProvider();
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
		this.btnZoomIn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				WorldMapViewPart.this.zoomIn();
			}
		});

		this.btnZoomOut = new Button(cmpBasicControls, SWT.FLAT | SWT.CENTER);
		this.btnZoomOut.setSize(25, 25);
		this.btnZoomOut.setText("-");
		this.btnZoomOut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				WorldMapViewPart.this.zoomOut();
			}
		});

		CLabel lblFloor = new CLabel(cmpBasicControls, SWT.NONE);
		lblFloor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblFloor.setText("Floor");

		this.cmbFloorViewer = new ComboViewer(cmpBasicControls, SWT.READ_ONLY);
		Combo cmbFloor = this.cmbFloorViewer.getCombo();
		cmbFloor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		this.cmbFloorViewer.setContentProvider(this.floorContentProvider);
		this.cmbFloorViewer.addSelectionChangedListener(this.selectionProvider);
		this.cmbFloorViewer.setLabelProvider(new TypeSafeViewerLabelProvider<ContinentFloor>(ContinentFloor.class) {
			@Override
			protected String getTypeSafeText(final ContinentFloor element) {
				return String.valueOf(element.getIndex());
			}
		});

		CLabel lblContinent = new CLabel(cmpBasicControls, SWT.NONE);
		lblContinent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblContinent.setText("Continent");

		this.cmbContinentViewer = new ComboViewer(cmpBasicControls, SWT.READ_ONLY);
		Combo cmbContinent = this.cmbContinentViewer.getCombo();
		cmbContinent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		this.cmbContinentViewer.setContentProvider(this.continentContentProvier);
		this.cmbContinentViewer.setLabelProvider(new TypeSafeViewerLabelProvider<Continent>(Continent.class) {
			@Override
			protected String getTypeSafeText(final Continent element) {
				return element.getName();
			}
		});
		this.cmbContinentViewer.setInput(this.wrapper);
		this.cmbContinentViewer.addSelectionChangedListener(this.selectionProvider);

		this.map = new MapWidget(parent, this.wrapper);
		GridLayout gridLayout = (GridLayout) this.map.getLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		this.map.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		this.map.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		this.createActions();
		this.initializeToolBar();
		this.initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
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
	public void init(final IViewSite site) throws PartInitException {
		checkNotNull(site, "missing site");
		super.init(site);
		site.setSelectionProvider(this.selectionProvider);
		site.getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	private Optional<ContinentFloor> getSelectedFloor() {
		final IStructuredSelection structuredSelection = (IStructuredSelection) this.cmbFloorViewer.getSelection();
		if (!structuredSelection.isEmpty()) {
			return Optional.of((ContinentFloor) structuredSelection.getFirstElement());
		} else {
			return Optional.absent();
		}
	}

	private Optional<Continent> getSelectedContinent() {
		final IStructuredSelection structuredSelection = (IStructuredSelection) this.cmbContinentViewer.getSelection();
		if (!structuredSelection.isEmpty()) {
			return Optional.of((Continent) structuredSelection.getFirstElement());
		} else {
			return Optional.absent();
		}
	}

	private void resetZoom() {
		this.setZoom(100);
	}

	private void setZoom(final int zoom) {
		final Optional<Continent> continent = this.getSelectedContinent();
		if (continent.isPresent()) {
			final int newZoom = Math.max(Math.min(zoom, (continent.get().getMaxZoom() * 100) + (100 - ZOOM_DELTA)), continent.get().getMinZoom() * 100);
			LOGGER.info("Zooming from {} to {}", this.zoom, newZoom);
			this.zoom = newZoom;
			this.getViewSite().getShell().getDisplay().syncExec(() -> {
				this.txtZoom.setText(newZoom + "%");
			});
			this.map.setZoomAndUpdate(this.zoom);
		} else {
			this.getViewSite().getShell().getDisplay().syncExec(() -> {
				this.txtZoom.setText("");
			});
		}
	}

	private void zoomIn() {
		this.setZoom(this.zoom + ZOOM_DELTA);
	}

	private void zoomOut() {
		this.setZoom(this.zoom - ZOOM_DELTA);
	}

	private void selectFloor(final ContinentFloor floor) {
		checkNotNull(floor, "missing floor");
		LOGGER.info("Select floor: {}", floor);
		this.map.setFloorAndUpdate(floor);
	}

	private void selectContinent(final Continent continent) {
		checkNotNull(continent, "missing continent");
		LOGGER.info("Select continent: {}", continent);
		this.resetZoom();
		this.cmbFloorViewer.setInput(continent);
		this.map.setContinentAndUpdate(continent);
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		checkNotNull(part, "missing part");
		checkNotNull(selection, "missing selection");
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (!structuredSelection.isEmpty()) {
			if (structuredSelection.getFirstElement() instanceof Continent) {
				this.selectContinent((Continent) structuredSelection.getFirstElement());
			} else if (structuredSelection.getFirstElement() instanceof ContinentFloor) {
				this.selectFloor((ContinentFloor) structuredSelection.getFirstElement());
			}
		}
	}

}
