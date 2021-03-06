package de.justi.yagw2api.explorer.rcp.wvw;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * yagw2api.explorer.rcp.application.wvw
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

import com.google.common.base.Optional;

import de.justi.yagw2api.explorer.rcp.Activator;
import de.justi.yagw2api.explorer.rcp.swt.AggregatingSelectionProvider;
import de.justi.yagw2api.explorer.rcp.swt.FixedColoredTypeSafeColumnLabelProvider;
import de.justi.yagw2api.explorer.rcp.swt.TypeSafeColumnLabelProvider;
import de.justi.yagw2api.explorer.rcp.swt.TypeSafeContentProvider;
import de.justi.yagw2api.explorer.rcp.swt.TypeSafeTableViewerColumnSorter;
import de.justi.yagw2api.explorer.rcp.swt.TypeSafeViewerLabelProvider;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMap;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMatch;
import de.justi.yagw2api.wrapper.wvw.event.WVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWMatchListener;
import de.justi.yagw2api.wrapper.wvw.event.WVWMatchScoresChangedEvent;

public class MatchDetailsViewPart extends ViewPart implements ISelectionListener, ISelectionChangedListener, WVWMatchListener {
	private static final class ContentProvider extends TypeSafeContentProvider<WVWMatch> {

		private final TableColumn redPointsColumn;
		private final TableColumn greenPointsColumn;
		private final TableColumn bluePointsColumn;
		private final TableColumn redTickColumn;
		private final TableColumn greenTickColumn;
		private final TableColumn blueTickColumn;

		public ContentProvider(final TableColumn redPointsColumn, final TableColumn greenPointsColumn, final TableColumn bluePointsColumn, final TableColumn redTickColumn,
				final TableColumn greenTickColumn, final TableColumn blueTickColumn) {
			super(WVWMatch.class);
			this.redPointsColumn = checkNotNull(redPointsColumn, "missing red points column");
			this.greenPointsColumn = checkNotNull(greenPointsColumn, "missing green points column");
			this.bluePointsColumn = checkNotNull(bluePointsColumn, "missing blue points column");
			this.redTickColumn = checkNotNull(redTickColumn, "missing red tick column");
			this.greenTickColumn = checkNotNull(greenTickColumn, "missing green tick column");
			this.blueTickColumn = checkNotNull(blueTickColumn, "missing blue tick column");
		}

		@Override
		protected Object[] getTypeSafeElements(final WVWMatch inputElement) {
			if (inputElement != null) {
				return new WVWMap[] { inputElement.getRedMap(), inputElement.getGreenMap(), inputElement.getBlueMap(), inputElement.getCenterMap() };
			} else {
				return super.getTypeSafeElements(inputElement);
			}
		}

		@Override
		public void dispose() {
		}

		@Override
		public void typeSafeInputChanged(final Viewer viewer, final WVWMatch oldInput, final WVWMatch newInput) {
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
			this.matchSelectionComboViewer.setLabelProvider(new TypeSafeViewerLabelProvider<WVWMatch>(WVWMatch.class) {
				@Override
				protected String getTypeSafeText(final WVWMatch element) {
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
						tableViewerColumn.setLabelProvider(new TypeSafeColumnLabelProvider<WVWMap>(WVWMap.class) {
							@Override
							public String getTypeSafeText(final WVWMap element) {
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
							protected Color getTypeSafeBackground(final WVWMap element) {
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
							protected Color getTypeSafeForeground(final WVWMap element) {
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
						new TypeSafeTableViewerColumnSorter<WVWMap>(tableViewerColumn, WVWMap.class) {
							@Override
							protected String getTypeSafeValue(final WVWMap o) {
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
						tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<WVWMap>(WVWMap.class, WVWUIConstants.RGB_RED_WORLD_BG,
								WVWUIConstants.RGB_RED_WORLD_FG) {
							@Override
							public String getTypeSafeText(final WVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.getScores().getRedScore());
							}
						});
						new TypeSafeTableViewerColumnSorter<WVWMap>(tableViewerColumn, WVWMap.class) {
							@Override
							protected Integer getTypeSafeValue(final WVWMap o) {
								return o.getScores().getRedScore();
							}
						};
						tblclmnRedpoints = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnRedpoints, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnRedpoints.setText("Red-Points");
					}
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<WVWMap>(WVWMap.class, WVWUIConstants.RGB_GREEN_WORLD_BG,
								WVWUIConstants.RGB_GREEN_WORLD_FG) {
							@Override
							public String getTypeSafeText(final WVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.getScores().getGreenScore());
							}
						});
						new TypeSafeTableViewerColumnSorter<WVWMap>(tableViewerColumn, WVWMap.class) {
							@Override
							protected Integer getTypeSafeValue(final WVWMap o) {
								return o.getScores().getGreenScore();
							}
						};
						tblclmnGreenpoints = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnGreenpoints, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnGreenpoints.setText("Green-Points");
					}
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<WVWMap>(WVWMap.class, WVWUIConstants.RGB_BLUE_WORLD_BG,
								WVWUIConstants.RGB_BLUE_WORLD_FG) {
							@Override
							public String getTypeSafeText(final WVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.getScores().getBlueScore());
							}
						});
						new TypeSafeTableViewerColumnSorter<WVWMap>(tableViewerColumn, WVWMap.class) {
							@Override
							protected Integer getTypeSafeValue(final WVWMap o) {
								return o.getScores().getBlueScore();
							}
						};
						tblclmnBluepoints = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnBluepoints, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnBluepoints.setText("Blue-Points");
					}
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<WVWMap>(WVWMap.class, WVWUIConstants.RGB_RED_WORLD_BG,
								WVWUIConstants.RGB_RED_WORLD_FG) {
							@Override
							public String getTypeSafeText(final WVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.calculateRedTick());
							}
						});
						new TypeSafeTableViewerColumnSorter<WVWMap>(tableViewerColumn, WVWMap.class) {
							@Override
							protected Integer getTypeSafeValue(final WVWMap o) {
								return o.calculateRedTick();
							}
						};
						tblclmnRedtick = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnRedtick, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnRedtick.setText("Red-Tick");
					}
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<WVWMap>(WVWMap.class, WVWUIConstants.RGB_GREEN_WORLD_BG,
								WVWUIConstants.RGB_GREEN_WORLD_FG) {
							@Override
							public String getTypeSafeText(final WVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.calculateGreenTick());
							}
						});
						new TypeSafeTableViewerColumnSorter<WVWMap>(tableViewerColumn, WVWMap.class) {
							@Override
							protected Integer getTypeSafeValue(final WVWMap o) {
								return o.calculateGreenTick();
							}
						};
						tblclmnGreentick = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnGreentick, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnGreentick.setText("Green-Tick");
					}
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<WVWMap>(WVWMap.class, WVWUIConstants.RGB_BLUE_WORLD_BG,
								WVWUIConstants.RGB_BLUE_WORLD_FG) {
							@Override
							public String getTypeSafeText(final WVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.calculateBlueTick());
							}
						});
						new TypeSafeTableViewerColumnSorter<WVWMap>(tableViewerColumn, WVWMap.class) {
							@Override
							protected Integer getTypeSafeValue(final WVWMap o) {
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

	private Optional<WVWMatch> getSelectedMatch() {
		if (this.matchSelectionComboViewer.getSelection().isEmpty()) {
			return Optional.absent();
		} else {
			final IStructuredSelection selection = (IStructuredSelection) this.matchSelectionComboViewer.getSelection();
			checkState(selection.getFirstElement() instanceof WVWMatch, "expected %s to be instance of %s", selection.getFirstElement(), WVWMatch.class);
			return Optional.of((WVWMatch) selection.getFirstElement());
		}
	}

	private Optional<WVWMap> getSelectedMap() {
		if (this.mapsTableViewer.getSelection().isEmpty()) {
			return Optional.absent();
		} else {
			final IStructuredSelection selection = (IStructuredSelection) this.mapsTableViewer.getSelection();
			checkState(selection.getFirstElement() instanceof WVWMap, "expected %s to be instance of %s", selection.getFirstElement(), WVWMap.class);
			return Optional.of((WVWMap) selection.getFirstElement());
		}
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		// handle selection events from workbench
		checkNotNull(part, "missing part");
		checkNotNull(selection, "missing selection");
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (!structuredSelection.isEmpty()) {
			if (structuredSelection.getFirstElement() instanceof WVWMatch) {
				this.selectMatch((WVWMatch) structuredSelection.getFirstElement());
			} else if (structuredSelection.getFirstElement() instanceof WVWMap) {
				this.selectMap((WVWMap) structuredSelection.getFirstElement());
			}
		}
	}

	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		// handle selection of views combo box
		checkNotNull(event, "missing event");
		final IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
		if (!structuredSelection.isEmpty() && structuredSelection.getFirstElement() instanceof WVWMatch) {
			this.selectMatch((WVWMatch) structuredSelection.getFirstElement());
		}
	}

	private synchronized void selectMatch(final WVWMatch match) {
		checkNotNull(match, "missing match");
		final Optional<WVWMatch> currentMatchSelection = this.getSelectedMatch();
		if (!currentMatchSelection.isPresent() || !currentMatchSelection.get().equals(match)) {
			LOGGER.trace("Select match: {}", match);
			this.matchSelectionComboViewer.setSelection(new StructuredSelection(match));
		}
		if (this.mapsTableViewer.getInput() == null || !this.mapsTableViewer.getInput().equals(match)) {
			this.mapsTableViewer.setInput(match);
		}
	}

	private synchronized void selectMap(final WVWMap map) {
		checkNotNull(map, "missing map");
		final Optional<WVWMap> currentMapSelection = this.getSelectedMap();
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
	public void onInitializedMatchForWrapper(final WVWInitializedMatchEvent e) {
		this.refreshUIForMatchesUpdate();
	}

	@Override
	public void onMatchScoreChangedEvent(final WVWMatchScoresChangedEvent e) {
		// NOP
	}

}
