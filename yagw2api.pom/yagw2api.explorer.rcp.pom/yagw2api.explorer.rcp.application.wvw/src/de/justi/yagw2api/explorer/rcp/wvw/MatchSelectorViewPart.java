package de.justi.yagw2api.explorer.rcp.wvw;

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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import de.justi.yagw2api.explorer.rcp.swt.TypeSafeTableViewerColumnSorter;
import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchListener;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;

public class MatchSelectorViewPart extends ViewPart implements IWVWMatchListener, ISelectionListener {
	public static final String ID = "yagw2api.explorer.rcp.wvw.matchselector"; //$NON-NLS-1$

	private static final Logger LOGGER = LoggerFactory.getLogger(MatchSelectorViewPart.class);
	private final AggregatingSelectionProvider selectionProvider;
	private final MatchesContentProvider contentProvider;
	private Table matchesTable = null;
	private TableViewer matchesTableViewer = null;

	public MatchSelectorViewPart() {
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
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		container.setLayout(new GridLayout(1, false));
		{
			Composite composite = new Composite(container, SWT.NONE);
			composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			TableColumnLayout tcl_composite = new TableColumnLayout();
			composite.setLayout(tcl_composite);
			{
				this.matchesTableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
				this.matchesTableViewer.addSelectionChangedListener(this.selectionProvider);
				this.matchesTableViewer.setContentProvider(this.contentProvider);
				this.matchesTableViewer.setInput(Activator.getDefault().getWVW());
				this.matchesTable = this.matchesTableViewer.getTable();
				this.matchesTable.setHeaderVisible(true);
				this.matchesTable.setLinesVisible(true);
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.matchesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new TypeSafeColumnLabelProvider<IWVWMatch>(IWVWMatch.class) {
						@Override
						protected String getTypeSafeText(final IWVWMatch element) {
							return element.getRedWorld().getWorldLocation().toString();
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWMatch>(tableViewerColumn, IWVWMatch.class) {
						@Override
						protected String getTypeSafeValue(final IWVWMatch o) {
							return o.getRedWorld().getWorldLocation().toString();
						}
					};
					TableColumn tblclmnContinent = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnContinent, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					tblclmnContinent.setText("Continent");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.matchesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new TypeSafeColumnLabelProvider<IWVWMatch>(IWVWMatch.class) {
						@Override
						protected String getTypeSafeText(final IWVWMatch element) {
							return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.getScores().getRedScore() + element.getScores().getGreenScore()
									+ element.getScores().getBlueScore());
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWMatch>(tableViewerColumn, IWVWMatch.class) {
						@Override
						protected Integer getTypeSafeValue(final IWVWMatch o) {
							return o.getScores().getRedScore() + o.getScores().getGreenScore() + o.getScores().getBlueScore();
						}
					};
					TableColumn tblclmnTotalPoints = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnTotalPoints, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					tblclmnTotalPoints.setText("Total Points");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.matchesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMatch>(IWVWMatch.class, WVWUIConstants.RGB_RED_WORLD_BG,
							WVWUIConstants.RGB_RED_WORLD_FG) {
						@Override
						protected String getTypeSafeText(final IWVWMatch element) {
							return element.getRedWorld().getName().or("");
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWMatch>(tableViewerColumn, IWVWMatch.class) {
						@Override
						protected String getTypeSafeValue(final IWVWMatch o) {
							return o.getRedWorld().getName().orNull();
						}
					};
					TableColumn tblclmnRedworld = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnRedworld, new ColumnWeightData(1, true));
					tblclmnRedworld.setText("Red-World");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.matchesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMatch>(IWVWMatch.class, WVWUIConstants.RGB_GREEN_WORLD_BG,
							WVWUIConstants.RGB_GREEN_WORLD_FG) {
						@Override
						protected String getTypeSafeText(final IWVWMatch element) {
							return element.getGreenWorld().getName().or("");
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWMatch>(tableViewerColumn, IWVWMatch.class) {
						@Override
						protected String getTypeSafeValue(final IWVWMatch o) {
							return o.getGreenWorld().getName().orNull();
						}
					};
					TableColumn tblclmnGreenworld = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnGreenworld, new ColumnWeightData(1, true));
					tblclmnGreenworld.setText("Green-World");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.matchesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMatch>(IWVWMatch.class, WVWUIConstants.RGB_BLUE_WORLD_BG,
							WVWUIConstants.RGB_BLUE_WORLD_FG) {
						@Override
						protected String getTypeSafeText(final IWVWMatch element) {
							return element.getBlueWorld().getName().or("");
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWMatch>(tableViewerColumn, IWVWMatch.class) {
						@Override
						protected String getTypeSafeValue(final IWVWMatch o) {
							return o.getBlueWorld().getName().orNull();
						}
					};
					TableColumn tblclmnBlueworld = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnBlueworld, new ColumnWeightData(1, true));
					tblclmnBlueworld.setText("Blue-World");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.matchesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMatch>(IWVWMatch.class, WVWUIConstants.RGB_RED_WORLD_BG,
							WVWUIConstants.RGB_RED_WORLD_FG) {
						@Override
						protected String getTypeSafeText(final IWVWMatch element) {
							return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.getScores().getRedScore());
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWMatch>(tableViewerColumn, IWVWMatch.class) {
						@Override
						protected Integer getTypeSafeValue(final IWVWMatch o) {
							return o.getScores().getRedScore();
						}
					};
					TableColumn tblclmnRedpoints = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnRedpoints, new ColumnWeightData(1, true));
					tblclmnRedpoints.setText("Red-Points");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.matchesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMatch>(IWVWMatch.class, WVWUIConstants.RGB_GREEN_WORLD_BG,
							WVWUIConstants.RGB_GREEN_WORLD_FG) {
						@Override
						protected String getTypeSafeText(final IWVWMatch element) {
							return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.getScores().getGreenScore());
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWMatch>(tableViewerColumn, IWVWMatch.class) {
						@Override
						protected Integer getTypeSafeValue(final IWVWMatch o) {
							return o.getScores().getGreenScore();
						}
					};
					TableColumn tblclmnGreenpoints = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnGreenpoints, new ColumnWeightData(1, true));
					tblclmnGreenpoints.setText("Green-Points");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.matchesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMatch>(IWVWMatch.class, WVWUIConstants.RGB_BLUE_WORLD_BG,
							WVWUIConstants.RGB_BLUE_WORLD_FG) {
						@Override
						protected String getTypeSafeText(final IWVWMatch element) {
							return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.getScores().getBlueScore());
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWMatch>(tableViewerColumn, IWVWMatch.class) {
						@Override
						protected Integer getTypeSafeValue(final IWVWMatch o) {
							return o.getScores().getBlueScore();
						}
					};
					TableColumn tblclmnBluepoints = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnBluepoints, new ColumnWeightData(1, true));
					tblclmnBluepoints.setText("Blue-Points");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.matchesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMatch>(IWVWMatch.class, WVWUIConstants.RGB_RED_WORLD_BG,
							WVWUIConstants.RGB_RED_WORLD_FG) {
						@Override
						protected String getTypeSafeText(final IWVWMatch element) {
							return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.calculateRedTick());
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWMatch>(tableViewerColumn, IWVWMatch.class) {
						@Override
						protected Integer getTypeSafeValue(final IWVWMatch o) {
							return o.calculateRedTick();
						}
					};
					TableColumn tblclmnRedtick = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnRedtick, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					tblclmnRedtick.setText("Red-Tick");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.matchesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMatch>(IWVWMatch.class, WVWUIConstants.RGB_GREEN_WORLD_BG,
							WVWUIConstants.RGB_GREEN_WORLD_FG) {
						@Override
						protected String getTypeSafeText(final IWVWMatch element) {
							return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.calculateGreenTick());
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWMatch>(tableViewerColumn, IWVWMatch.class) {
						@Override
						protected Integer getTypeSafeValue(final IWVWMatch o) {
							return o.calculateGreenTick();
						}
					};
					TableColumn tblclmnGreentick = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnGreentick, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					tblclmnGreentick.setText("Green-Tick");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.matchesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new FixedColoredTypeSafeColumnLabelProvider<IWVWMatch>(IWVWMatch.class, WVWUIConstants.RGB_BLUE_WORLD_BG,
							WVWUIConstants.RGB_BLUE_WORLD_FG) {
						@Override
						protected String getTypeSafeText(final IWVWMatch element) {
							return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.calculateBlueTick());
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWMatch>(tableViewerColumn, IWVWMatch.class) {
						@Override
						protected Integer getTypeSafeValue(final IWVWMatch o) {
							return o.calculateBlueTick();
						}
					};
					TableColumn tblclmnBluetick = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnBluetick, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					tblclmnBluetick.setText("Blue-Tick");
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

	private void refreshUIForMatchUpdate() {
		if (this.matchesTableViewer != null) {
			Display.getDefault().asyncExec(() -> {
				if (!this.matchesTable.isDisposed()) {
					this.matchesTableViewer.refresh();
				}
			});
		}
	}

	private Optional<IWVWMatch> getSelectedMatch() {
		if (this.matchesTableViewer.getSelection().isEmpty()) {
			return Optional.absent();
		} else {
			final IStructuredSelection selection = (IStructuredSelection) this.matchesTableViewer.getSelection();
			checkState(selection.getFirstElement() instanceof IWVWMatch, "expected %s to be instance of %s", selection.getFirstElement(), IWVWMatch.class);
			return Optional.of((IWVWMatch) selection.getFirstElement());
		}
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		checkNotNull(part, "missing part");
		checkNotNull(selection, "missing selection");
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (!structuredSelection.isEmpty()) {
			if (structuredSelection.getFirstElement() instanceof IWVWMatch) {
				this.selectMatch((IWVWMatch) structuredSelection.getFirstElement());
			}
		}
	}

	private synchronized void selectMatch(final IWVWMatch match) {
		checkNotNull(match, "missing match");
		final Optional<IWVWMatch> currentMatchSelection = this.getSelectedMatch();
		if (!currentMatchSelection.isPresent() || !currentMatchSelection.get().equals(match)) {
			LOGGER.trace("Select match: {}", match);
			this.matchesTableViewer.setSelection(new StructuredSelection(match));
		}
	}

	@Override
	public void dispose() {
		Activator.getDefault().getWVW().unregisterWVWMatchListener(this);
	}

	@Override
	public void onInitializedMatchForWrapper(final IWVWInitializedMatchEvent arg0) {
		this.refreshUIForMatchUpdate();
	}

	@Override
	public void onMatchScoreChangedEvent(final IWVWMatchScoresChangedEvent arg0) {
		this.refreshUIForMatchUpdate();
	}

}
