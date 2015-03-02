package yagw2api.explorer.rcp.wvw;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Explorer-RCP-Application
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yagw2api.explorer.rcp.Activator;
import yagw2api.explorer.rcp.swt.AggregatingSelectionProvider;
import yagw2api.explorer.rcp.swt.FixedColoredTypeSafeColumnLabelProvider;
import yagw2api.explorer.rcp.swt.TypeSafeColumnLabelProvider;
import yagw2api.explorer.rcp.swt.TypeSafeContentProvider;
import yagw2api.explorer.rcp.swt.TypeSafeTableViewerColumnSorter;
import yagw2api.explorer.rcp.swt.TypeSafeViewerLabelProvider;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchListener;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;

public class MatchDetailsViewPart extends ViewPart implements ISelectionListener, ISelectionChangedListener, IWVWMatchListener {
	private static final class ContentProvider extends TypeSafeContentProvider<IWVWMatch> {

		private final TableColumn redPointsColumn;
		private final TableColumn greenPointsColumn;
		private final TableColumn bluePointsColumn;
		private final TableColumn redTickColumn;
		private final TableColumn greenTickColumn;
		private final TableColumn blueTickColumn;

		public ContentProvider(final TableColumn redPointsColumn, final TableColumn greenPointsColumn, final TableColumn bluePointsColumn, final TableColumn redTickColumn,
				final TableColumn greenTickColumn, final TableColumn blueTickColumn) {
			super(IWVWMatch.class);
			this.redPointsColumn = checkNotNull(redPointsColumn, "missing red points column");
			this.greenPointsColumn = checkNotNull(greenPointsColumn, "missing green points column");
			this.bluePointsColumn = checkNotNull(bluePointsColumn, "missing blue points column");
			this.redTickColumn = checkNotNull(redTickColumn, "missing red tick column");
			this.greenTickColumn = checkNotNull(greenTickColumn, "missing green tick column");
			this.blueTickColumn = checkNotNull(blueTickColumn, "missing blue tick column");
		}

		@Override
		protected Object[] getTypeSafeElements(final IWVWMatch inputElement) {
			if (inputElement != null) {
				return new IWVWMap[] { inputElement.getRedMap(), inputElement.getGreenMap(), inputElement.getBlueMap(), inputElement.getCenterMap() };
			} else {
				return super.getTypeSafeElements(inputElement);
			}
		}

		@Override
		public void dispose() {
		}

		@Override
		public void typeSafeInputChanged(final Viewer viewer, final IWVWMatch oldInput, final IWVWMatch newInput) {
			if (newInput != null) {
				this.redPointsColumn.setText(newInput.getRedWorld().getName().or("Red") + "-Points");
				this.greenPointsColumn.setText(newInput.getGreenWorld().getName().or("Green") + "-Points");
				this.bluePointsColumn.setText(newInput.getBlueWorld().getName().or("Blue") + "-Points");
				this.redTickColumn.setText(newInput.getRedWorld().getName().or("Red") + "-Tick");
				this.greenTickColumn.setText(newInput.getGreenWorld().getName().or("Green") + "-Tick");
				this.blueTickColumn.setText(newInput.getBlueWorld().getName().or("Blue") + "-Tick");
			} else {
				this.redTickColumn.setText("Red-Tick");
				this.greenTickColumn.setText("Green-Tick");
				this.blueTickColumn.setText("Blue-Tick");
			}
		}
	}

	public static final String ID = "yagw2api.explorer.rcp.wvw.matchdetails"; //$NON-NLS-1$
	private static final Logger LOGGER = LoggerFactory.getLogger(MatchDetailsViewPart.class);

	private final AggregatingSelectionProvider selectionProvider;
	private final MatchesContentProvider contentProvider;
	private ComboViewer matchSelectionComboViewer;
	private Combo matchSelectionCombo;
	private Table mapsTable;
	private TableViewer mapsTableViewer;

	public MatchDetailsViewPart() {
		this.selectionProvider = new AggregatingSelectionProvider();
		this.contentProvider = new MatchesContentProvider();
		Activator.getDefault().getWVW().registerWVWMatchListener(this);
	}

	/**
	 * Create contents of the view part.
	 *
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		container.setLayout(new GridLayout(2, false));
		{
			CLabel lblNewLabel = new CLabel(container, SWT.NONE);
			lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
			lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			lblNewLabel.setText("Match");
		}
		{
			this.matchSelectionComboViewer = new ComboViewer(container, SWT.READ_ONLY);
			this.matchSelectionComboViewer.setLabelProvider(new TypeSafeViewerLabelProvider<IWVWMatch>(IWVWMatch.class) {
				@Override
				protected String getTypeSafeText(final IWVWMatch element) {
					return element.getRedWorld().getWorldLocation() + ": " + element.getRedWorld().getName().get() + " vs. " + element.getGreenWorld().getName().get() + " vs. "
							+ element.getBlueWorld().getName().get();
				}
			});
			this.matchSelectionComboViewer.setContentProvider(this.contentProvider);
			this.matchSelectionComboViewer.setInput(Activator.getDefault().getWVW());
			this.matchSelectionComboViewer.addSelectionChangedListener(this);
			this.matchSelectionComboViewer.addSelectionChangedListener(this.selectionProvider);
			this.matchSelectionCombo = this.matchSelectionComboViewer.getCombo();
			this.matchSelectionCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
			{
				Composite composite = new Composite(container, SWT.NONE);
				composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
				TableColumnLayout tcl_composite = new TableColumnLayout();
				composite.setLayout(tcl_composite);
				{
					this.mapsTableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
					this.mapsTableViewer.addSelectionChangedListener(this.selectionProvider);
					this.mapsTable = this.mapsTableViewer.getTable();
					this.mapsTable.setHeaderVisible(true);
					this.mapsTable.setLinesVisible(true);
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new TypeSafeColumnLabelProvider<IWVWMap>(IWVWMap.class) {
							@Override
							public String getTypeSafeText(final IWVWMap element) {
								final String fallback = element.getType().getLabel(Activator.getDefault().getLocale()).or(element.getType().toString());
								if (element.getMatch().isPresent()) {
									if (element.getType().isRed()) {
										return element.getMatch().get().getRedWorld().getName().or(fallback);
									} else if (element.getType().isGreen()) {
										return element.getMatch().get().getGreenWorld().getName().or(fallback);
									} else if (element.getType().isBlue()) {
										return element.getMatch().get().getBlueWorld().getName().or(fallback);
									} else {
										return fallback;
									}
								} else {
									return fallback;
								}
							}

							@Override
							protected Color getTypeSafeBackground(final IWVWMap element) {
								if (element.getType().isRed()) {
									return SWTResourceManager.getColor(WVWUIConstants.RGB_RED_WORLD_BG);
								} else if (element.getType().isGreen()) {
									return SWTResourceManager.getColor(WVWUIConstants.RGB_GREEN_WORLD_BG);
								} else if (element.getType().isBlue()) {
									return SWTResourceManager.getColor(WVWUIConstants.RGB_BLUE_WORLD_BG);
								} else {
									return super.getTypeSafeBackground(element);
								}
							}

							@Override
							protected Color getTypeSafeForeground(final IWVWMap element) {
								if (element.getType().isRed()) {
									return SWTResourceManager.getColor(WVWUIConstants.RGB_RED_WORLD_FG);
								} else if (element.getType().isGreen()) {
									return SWTResourceManager.getColor(WVWUIConstants.RGB_GREEN_WORLD_FG);
								} else if (element.getType().isBlue()) {
									return SWTResourceManager.getColor(WVWUIConstants.RGB_BLUE_WORLD_FG);
								} else {
									return super.getTypeSafeForeground(element);
								}
							}
						});
						new TypeSafeTableViewerColumnSorter<IWVWMap>(tableViewerColumn, IWVWMap.class) {
							@Override
							protected String getTypeSafeValue(final IWVWMap o) {
								return o.getType().toString();
							}
						};
						TableColumn tblclmnMap = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnMap, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnMap.setText("Map");
					}
					final TableColumn tblclmnRedpoints;
					final TableColumn tblclmnGreenpoints;
					final TableColumn tblclmnBluepoints;
					final TableColumn tblclmnRedtick;
					final TableColumn tblclmnGreentick;
					final TableColumn tblclmnBluetick;
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMap>(IWVWMap.class, WVWUIConstants.RGB_RED_WORLD_BG,
								WVWUIConstants.RGB_RED_WORLD_FG) {
							@Override
							public String getTypeSafeText(final IWVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.getScores().getRedScore());
							}
						});
						new TypeSafeTableViewerColumnSorter<IWVWMap>(tableViewerColumn, IWVWMap.class) {
							@Override
							protected Integer getTypeSafeValue(final IWVWMap o) {
								return o.getScores().getRedScore();
							}
						};
						tblclmnRedpoints = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnRedpoints, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnRedpoints.setText("Red-Points");
					}
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMap>(IWVWMap.class, WVWUIConstants.RGB_GREEN_WORLD_BG,
								WVWUIConstants.RGB_GREEN_WORLD_FG) {
							@Override
							public String getTypeSafeText(final IWVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.getScores().getGreenScore());
							}
						});
						new TypeSafeTableViewerColumnSorter<IWVWMap>(tableViewerColumn, IWVWMap.class) {
							@Override
							protected Integer getTypeSafeValue(final IWVWMap o) {
								return o.getScores().getGreenScore();
							}
						};
						tblclmnGreenpoints = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnGreenpoints, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnGreenpoints.setText("Green-Points");
					}
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMap>(IWVWMap.class, WVWUIConstants.RGB_BLUE_WORLD_BG,
								WVWUIConstants.RGB_BLUE_WORLD_FG) {
							@Override
							public String getTypeSafeText(final IWVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.getScores().getBlueScore());
							}
						});
						new TypeSafeTableViewerColumnSorter<IWVWMap>(tableViewerColumn, IWVWMap.class) {
							@Override
							protected Integer getTypeSafeValue(final IWVWMap o) {
								return o.getScores().getBlueScore();
							}
						};
						tblclmnBluepoints = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnBluepoints, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnBluepoints.setText("Blue-Points");
					}
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMap>(IWVWMap.class, WVWUIConstants.RGB_RED_WORLD_BG,
								WVWUIConstants.RGB_RED_WORLD_FG) {
							@Override
							public String getTypeSafeText(final IWVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.calculateRedTick());
							}
						});
						new TypeSafeTableViewerColumnSorter<IWVWMap>(tableViewerColumn, IWVWMap.class) {
							@Override
							protected Integer getTypeSafeValue(final IWVWMap o) {
								return o.calculateRedTick();
							}
						};
						tblclmnRedtick = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnRedtick, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnRedtick.setText("Red-Tick");
					}
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMap>(IWVWMap.class, WVWUIConstants.RGB_GREEN_WORLD_BG,
								WVWUIConstants.RGB_GREEN_WORLD_FG) {
							@Override
							public String getTypeSafeText(final IWVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.calculateGreenTick());
							}
						});
						new TypeSafeTableViewerColumnSorter<IWVWMap>(tableViewerColumn, IWVWMap.class) {
							@Override
							protected Integer getTypeSafeValue(final IWVWMap o) {
								return o.calculateGreenTick();
							}
						};
						tblclmnGreentick = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnGreentick, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnGreentick.setText("Green-Tick");
					}
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMap>(IWVWMap.class, WVWUIConstants.RGB_BLUE_WORLD_BG,
								WVWUIConstants.RGB_BLUE_WORLD_FG) {
							@Override
							public String getTypeSafeText(final IWVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.calculateBlueTick());
							}
						});
						new TypeSafeTableViewerColumnSorter<IWVWMap>(tableViewerColumn, IWVWMap.class) {
							@Override
							protected Integer getTypeSafeValue(final IWVWMap o) {
								return o.calculateBlueTick();
							}
						};
						tblclmnBluetick = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnBluetick, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnBluetick.setText("Blue-Tick");
					}
					this.mapsTableViewer.setContentProvider(new ContentProvider(tblclmnRedpoints, tblclmnGreenpoints, tblclmnBluepoints, tblclmnRedtick, tblclmnGreentick,
							tblclmnBluetick));
				}
			}
		}

		this.createActions();
		this.initializeToolBar();
		this.initializeMenu();
	}

	@Override
	public void init(final IViewSite site) throws PartInitException {
		checkNotNull(site, "missing site");
		super.init(site);
		site.setSelectionProvider(this.selectionProvider);
		site.getWorkbenchWindow().getSelectionService().addSelectionListener(this);
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

	private void refreshUIForMatchesUpdate() {
		Display.getDefault().asyncExec(() -> {
			if (this.matchSelectionCombo != null && !this.matchSelectionCombo.isDisposed()) {
				this.matchSelectionComboViewer.refresh();
			} else {
				LOGGER.warn("can't refresh match selection combo");
			}
		});
	}

	private Optional<IWVWMatch> getSelectedMatch() {
		if (this.matchSelectionComboViewer.getSelection().isEmpty()) {
			return Optional.absent();
		} else {
			final IStructuredSelection selection = (IStructuredSelection) this.matchSelectionComboViewer.getSelection();
			checkState(selection.getFirstElement() instanceof IWVWMatch, "expected %s to be instance of %s", selection.getFirstElement(), IWVWMatch.class);
			return Optional.of((IWVWMatch) selection.getFirstElement());
		}
	}

	private Optional<IWVWMap> getSelectedMap() {
		if (this.mapsTableViewer.getSelection().isEmpty()) {
			return Optional.absent();
		} else {
			final IStructuredSelection selection = (IStructuredSelection) this.mapsTableViewer.getSelection();
			checkState(selection.getFirstElement() instanceof IWVWMap, "expected %s to be instance of %s", selection.getFirstElement(), IWVWMap.class);
			return Optional.of((IWVWMap) selection.getFirstElement());
		}
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		// handle selection events from workbench
		checkNotNull(part, "missing part");
		checkNotNull(selection, "missing selection");
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (!structuredSelection.isEmpty()) {
			if (structuredSelection.getFirstElement() instanceof IWVWMatch) {
				this.selectMatch((IWVWMatch) structuredSelection.getFirstElement());
			} else if (structuredSelection.getFirstElement() instanceof IWVWMap) {
				this.selectMap((IWVWMap) structuredSelection.getFirstElement());
			}
		}
	}

	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		// handle selection of views combo box
		checkNotNull(event, "missing event");
		final IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
		if (!structuredSelection.isEmpty() && structuredSelection.getFirstElement() instanceof IWVWMatch) {
			this.selectMatch((IWVWMatch) structuredSelection.getFirstElement());
		}
	}

	private synchronized void selectMatch(final IWVWMatch match) {
		checkNotNull(match, "missing match");
		final Optional<IWVWMatch> currentMatchSelection = this.getSelectedMatch();
		if (!currentMatchSelection.isPresent() || !currentMatchSelection.get().equals(match)) {
			LOGGER.trace("Select match: {}", match);
			this.matchSelectionComboViewer.setSelection(new StructuredSelection(match));
		}
		if (this.mapsTableViewer.getInput() == null || !this.mapsTableViewer.getInput().equals(match)) {
			this.mapsTableViewer.setInput(match);
		}
	}

	private synchronized void selectMap(final IWVWMap map) {
		checkNotNull(map, "missing map");
		final Optional<IWVWMap> currentMapSelection = this.getSelectedMap();
		if (!currentMapSelection.isPresent() || !currentMapSelection.get().equals(map)) {
			LOGGER.trace("Select map: {}", map);
			this.mapsTableViewer.setSelection(new StructuredSelection(map));
		}
	}

	@Override
	public void dispose() {
		Activator.getDefault().getWVW().unregisterWVWMatchListener(this);
	}

	@Override
	public void onInitializedMatchForWrapper(final IWVWInitializedMatchEvent e) {
		this.refreshUIForMatchesUpdate();
	}

	@Override
	public void onMatchScoreChangedEvent(final IWVWMatchScoresChangedEvent e) {
		// NOP
	}

}
