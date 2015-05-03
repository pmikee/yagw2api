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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

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

import com.google.common.base.Function;
import com.google.common.base.Optional;

import de.justi.yagw2api.explorer.rcp.Activator;
import de.justi.yagw2api.explorer.rcp.swt.AggregatingSelectionProvider;
import de.justi.yagw2api.explorer.rcp.swt.TypeSafeContentProvider;
import de.justi.yagw2api.explorer.rcp.swt.TypeSafeTableViewerColumnSorter;
import de.justi.yagw2api.explorer.rcp.swt.TypeSafeViewerLabelProvider;
import de.justi.yagw2api.wrapper.guild.domain.Guild;
import de.justi.yagw2api.wrapper.world.domain.World;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMap;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMatch;
import de.justi.yagw2api.wrapper.wvw.domain.WVWObjective;
import de.justi.yagw2api.wrapper.wvw.event.WVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWMapListener;
import de.justi.yagw2api.wrapper.wvw.event.WVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWMatchListener;
import de.justi.yagw2api.wrapper.wvw.event.WVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveUnclaimedEvent;

public class MapDetailsViewPart extends ViewPart implements ISelectionListener, ISelectionChangedListener, WVWMatchListener, WVWMapListener {
	private static class MatchMapsContentProvider extends TypeSafeContentProvider<WVWMatch> {
		public MatchMapsContentProvider() {
			super(WVWMatch.class);
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
		protected void typeSafeInputChanged(final Viewer viewer, final WVWMatch oldInput, final WVWMatch newInput) {
		}
	}

	private static class MapObjectivesContentProvider extends TypeSafeContentProvider<WVWMap> {
		public MapObjectivesContentProvider() {
			super(WVWMap.class);
		}

		@Override
		protected Object[] getTypeSafeElements(final WVWMap inputElement) {
			if (inputElement != null) {
				return inputElement.getObjectives().toArray(new WVWObjective[inputElement.getObjectives().size()]);
			} else {
				return super.getTypeSafeElements(inputElement);
			}
		}

		@Override
		public void dispose() {
		}

		@Override
		protected void typeSafeInputChanged(final Viewer viewer, final WVWMap oldInput, final WVWMap newInput) {
		}
	}

	public static final String ID = "yagw2api.explorer.rcp.wvw.mapdetails"; //$NON-NLS-1$
	private static final Logger LOGGER = LoggerFactory.getLogger(MapDetailsViewPart.class);
	private static final Function<Optional<Guild>, String> GUILD_2_STRING = guild -> guild.isPresent() ? "[" + guild.get().getTag() + "] " + guild.get().getName() : "";
	private static final Function<Optional<World>, String> WORLD_2_STRING = world -> world.isPresent() ? world.get().getName().or(String.valueOf(world.get().getId())) : "";
	private static final Function<WVWObjective, String> OBJECTIVE_2_STRING = objective -> objective.getLabel().or(objective.getType().getLabel());

	private final AggregatingSelectionProvider selectionProvider;

	private Table mapObjectivesTable;
	private TableViewer mapObjectivesTableViewer;
	private ComboViewer matchSelectionComboViewer;
	private ComboViewer mapSelectionComboViewer;
	private Combo matchSelectionCombo;
	private Combo mapSelectionCombo;

	public MapDetailsViewPart() {
		this.selectionProvider = new AggregatingSelectionProvider();
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
			CLabel lblMatch = new CLabel(container, SWT.NONE);
			lblMatch.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			lblMatch.setText("Match");
		}
		{
			this.matchSelectionComboViewer = new ComboViewer(container, SWT.READ_ONLY);
			this.matchSelectionComboViewer.addSelectionChangedListener(this.selectionProvider);
			this.matchSelectionComboViewer.setLabelProvider(new TypeSafeViewerLabelProvider<WVWMatch>(WVWMatch.class) {
				@Override
				protected String getTypeSafeText(final WVWMatch element) {
					return element.getRedWorld().getWorldLocation() + ": " + element.getRedWorld().getName().get() + " vs. " + element.getGreenWorld().getName().get() + " vs. "
							+ element.getBlueWorld().getName().get();
				}
			});
			this.matchSelectionCombo = this.matchSelectionComboViewer.getCombo();
			this.matchSelectionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			this.matchSelectionComboViewer.setContentProvider(new MatchesContentProvider());
			this.matchSelectionComboViewer.setInput(Activator.getDefault().getWVW());
			this.matchSelectionComboViewer.addSelectionChangedListener(this);
		}
		{
			CLabel lblMap = new CLabel(container, SWT.NONE);
			lblMap.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			lblMap.setText("Map");
		}
		{
			this.mapSelectionComboViewer = new ComboViewer(container, SWT.READ_ONLY);
			this.mapSelectionComboViewer.addSelectionChangedListener(this.selectionProvider);
			this.mapSelectionComboViewer.setLabelProvider(new TypeSafeViewerLabelProvider<WVWMap>(WVWMap.class) {
				@Override
				protected String getTypeSafeText(final WVWMap element) {
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
			});
			this.mapSelectionCombo = this.mapSelectionComboViewer.getCombo();
			this.mapSelectionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			this.mapSelectionComboViewer.setContentProvider(new MatchMapsContentProvider());
			this.mapSelectionComboViewer.addSelectionChangedListener(this);
		}
		{
			Composite composite = new Composite(container, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
			TableColumnLayout tcl_composite = new TableColumnLayout();
			composite.setLayout(tcl_composite);
			{
				this.mapObjectivesTableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
				this.mapObjectivesTable = this.mapObjectivesTableViewer.getTable();
				this.mapObjectivesTable.setHeaderVisible(true);
				this.mapObjectivesTable.setLinesVisible(true);
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapObjectivesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new OwningWorldMatchingObjectiveColumnLabelProvider() {
						@Override
						protected String getTypeSafeText(final WVWObjective element) {
							return OBJECTIVE_2_STRING.apply(element);
						}
					});
					new TypeSafeTableViewerColumnSorter<WVWObjective>(tableViewerColumn, WVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final WVWObjective o) {
							return OBJECTIVE_2_STRING.apply(o);
						}
					};
					TableColumn tblclmnName = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnName, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					tblclmnName.setText("Name");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapObjectivesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new OwningWorldMatchingObjectiveColumnLabelProvider() {
						@Override
						protected String getTypeSafeText(final WVWObjective element) {
							return element.getType().getLabel();
						}
					});
					new TypeSafeTableViewerColumnSorter<WVWObjective>(tableViewerColumn, WVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final WVWObjective o) {
							return o.getType().getLabel();
						}
					};
					TableColumn tblclmnType = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnType, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					tblclmnType.setText("Type");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapObjectivesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new OwningWorldMatchingObjectiveColumnLabelProvider() {
						@Override
						protected String getTypeSafeText(final WVWObjective element) {
							return String.valueOf(element.getType().getPoints());
						}

					});
					new TypeSafeTableViewerColumnSorter<WVWObjective>(tableViewerColumn, WVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final WVWObjective o) {
							return o.getType().getPoints();
						}
					};
					TableColumn tblclmnValue = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnValue, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					tblclmnValue.setText("Value");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapObjectivesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new OwningWorldMatchingObjectiveColumnLabelProvider() {
						@Override
						protected String getTypeSafeText(final WVWObjective element) {
							return WORLD_2_STRING.apply(element.getOwner());
						}
					});
					new TypeSafeTableViewerColumnSorter<WVWObjective>(tableViewerColumn, WVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final WVWObjective o) {
							return WORLD_2_STRING.apply(o.getOwner());
						}
					};
					TableColumn tblclmnOwner = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnOwner, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					tblclmnOwner.setText("Owner");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapObjectivesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new OwningWorldMatchingObjectiveColumnLabelProvider() {
						@Override
						protected String getTypeSafeText(final WVWObjective element) {
							return GUILD_2_STRING.apply(element.getClaimedByGuild());
						}
					});
					new TypeSafeTableViewerColumnSorter<WVWObjective>(tableViewerColumn, WVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final WVWObjective o) {
							return GUILD_2_STRING.apply(o.getClaimedByGuild());
						}
					};
					TableColumn tblclmnClaimed = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnClaimed, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					tblclmnClaimed.setText("Claimed");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapObjectivesTableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new OwningWorldMatchingObjectiveColumnLabelProvider() {
						@Override
						protected String getTypeSafeText(final WVWObjective element) {
							return WVWUIConstants.DURATION_FORMAT.apply(Duration.of(element.getRemainingBuffDuration(TimeUnit.SECONDS), ChronoUnit.SECONDS));

						}
					});
					new TypeSafeTableViewerColumnSorter<WVWObjective>(tableViewerColumn, WVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final WVWObjective o) {
							return o.getRemainingBuffDuration(TimeUnit.SECONDS);
						}
					};
					TableColumn tblclmnBuff = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnBuff, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					tblclmnBuff.setText("Buff");
				}
				this.mapObjectivesTableViewer.setContentProvider(new MapObjectivesContentProvider());
			}
		}

		this.createActions();
		this.initializeToolBar();
		this.initializeMenu();

		this.initializeAutoRefresh();
	}

	private void initializeAutoRefresh() {
		final Runnable refresh = new Runnable() {
			@Override
			public void run() {
				if (MapDetailsViewPart.this.mapObjectivesTableViewer.getInput() != null) {
					MapDetailsViewPart.this.mapObjectivesTableViewer.refresh();
				}
				Display.getCurrent().timerExec((int) TimeUnit.SECONDS.toMillis(1), this);
			}
		};
		refresh.run();
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

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		checkNotNull(part, "missing part");
		checkNotNull(selection, "missing selection");
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.getFirstElement() instanceof WVWMatch) {
			this.selectMatch((WVWMatch) structuredSelection.getFirstElement());
		} else if (structuredSelection.getFirstElement() instanceof WVWMap) {
			this.selectMap((WVWMap) structuredSelection.getFirstElement());
		}
	}

	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		checkNotNull(event, "missing selection  event");
		final IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
		if (structuredSelection.getFirstElement() instanceof WVWMatch) {
			this.selectMatch((WVWMatch) structuredSelection.getFirstElement());
		} else if (structuredSelection.getFirstElement() instanceof WVWMap) {
			this.selectMap((WVWMap) structuredSelection.getFirstElement());
		}
	}

	private void refreshUIForMatchUpdate() {
		Display.getDefault().asyncExec(() -> {
			if (this.matchSelectionCombo != null && !this.matchSelectionCombo.isDisposed()) {
				LOGGER.trace("refresh ui for match update");
				this.matchSelectionComboViewer.refresh();
			} else {
				LOGGER.warn("can't refresh ui for match update");
			}
		});
	}

	private void refreshUIForMapUpdate() {
		Display.getDefault().asyncExec(() -> {
			if (this.mapObjectivesTable != null && !this.mapObjectivesTable.isDisposed()) {
				LOGGER.trace("refresh ui for map update");
				this.mapObjectivesTableViewer.refresh();
			} else {
				LOGGER.warn("can't refresh ui for map update");
			}
		});
	}

	private Optional<WVWMap> getSelectedMap() {
		if (this.mapSelectionComboViewer.getSelection().isEmpty()) {
			return Optional.absent();
		} else {
			final IStructuredSelection selection = (IStructuredSelection) this.mapSelectionComboViewer.getSelection();
			checkState(selection.getFirstElement() instanceof WVWMap, "expected %s to be instance of %s", selection.getFirstElement(), WVWMap.class);
			return Optional.of((WVWMap) selection.getFirstElement());
		}
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

	private synchronized void selectMatch(final WVWMatch match) {
		checkNotNull(match, "missing match");
		final Optional<WVWMatch> currentMatchSelection = this.getSelectedMatch();
		if (!currentMatchSelection.isPresent() || !currentMatchSelection.get().equals(match)) {
			LOGGER.trace("Select match: {}", match);
			this.matchSelectionComboViewer.setSelection(new StructuredSelection(match));
			this.mapSelectionComboViewer.setInput(match);
		}
		final Optional<WVWMap> currentMapSelection = this.getSelectedMap();
		if (currentMapSelection.isPresent()) {
			LOGGER.trace("Clear map selection for {}", match);
			this.mapSelectionComboViewer.setSelection(StructuredSelection.EMPTY);
			this.mapObjectivesTableViewer.setInput(null);
			Activator.getDefault().getWVW().unregisterWVWMapListener(this);
		}
	}

	private synchronized void selectMap(final WVWMap map) {
		checkNotNull(map, "missing map");
		final Optional<WVWMap> currentMapSelection = this.getSelectedMap();
		if (!currentMapSelection.isPresent() || !currentMapSelection.get().equals(map)) {
			LOGGER.trace("Select map: {}", map);
			final Optional<WVWMatch> currentMatchSelection = this.getSelectedMatch();
			if (!currentMatchSelection.isPresent() && map.getMatch().isPresent()) {
				this.selectMatch(map.getMatch().get());
			}
			this.mapSelectionComboViewer.setSelection(new StructuredSelection(map));
		}
		if (this.mapObjectivesTableViewer.getInput() == null || !this.mapObjectivesTableViewer.getInput().equals(map)) {
			this.mapObjectivesTableViewer.setInput(map);
			Activator.getDefault().getWVW().unregisterWVWMapListener(this);
			Activator.getDefault().getWVW().registerWVWMapListener(map, this);
		}
	}

	@Override
	public void dispose() {
		Activator.getDefault().getWVW().unregisterWVWMatchListener(this);
		Activator.getDefault().getWVW().unregisterWVWMapListener(this);
	}

	@Override
	public void onInitializedMatchForWrapper(final WVWInitializedMatchEvent e) {
		this.refreshUIForMatchUpdate();
	}

	@Override
	public void onMatchScoreChangedEvent(final WVWMatchScoresChangedEvent e) {
		this.refreshUIForMatchUpdate();
	}

	@Override
	public void onChangedMapScoreEvent(final WVWMapScoresChangedEvent e) {
		this.refreshUIForMapUpdate();
	}

	@Override
	public void onObjectiveCapturedEvent(final WVWObjectiveCaptureEvent e) {
		this.refreshUIForMapUpdate();
	}

	@Override
	public void onObjectiveClaimedEvent(final WVWObjectiveClaimedEvent e) {
		this.refreshUIForMapUpdate();
	}

	@Override
	public void onObjectiveEndOfBuffEvent(final WVWObjectiveEndOfBuffEvent e) {
		this.refreshUIForMapUpdate();
	}

	@Override
	public void onObjectiveUnclaimedEvent(final WVWObjectiveUnclaimedEvent e) {
		this.refreshUIForMapUpdate();
	}
}
