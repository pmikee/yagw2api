package yagw2api.explorer.rcp.wvw;

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

import yagw2api.explorer.rcp.Activator;
import yagw2api.explorer.rcp.swt.AggregatingSelectionProvider;
import yagw2api.explorer.rcp.swt.TypeSafeContentProvider;
import yagw2api.explorer.rcp.swt.TypeSafeTableViewerColumnSorter;
import yagw2api.explorer.rcp.swt.TypeSafeViewerLabelProvider;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.IGuild;
import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMapListener;
import de.justi.yagw2api.wrapper.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchListener;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveUnclaimedEvent;
import de.justi.yagw2api.wrapper.IWorld;

public class MapDetailsViewPart extends ViewPart implements ISelectionListener, ISelectionChangedListener, IWVWMatchListener, IWVWMapListener {
	private static class MatchMapsContentProvider extends TypeSafeContentProvider<IWVWMatch> {
		public MatchMapsContentProvider() {
			super(IWVWMatch.class);
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
		protected void typeSafeInputChanged(final Viewer viewer, final IWVWMatch oldInput, final IWVWMatch newInput) {
		}
	}

	private static class MapObjectivesContentProvider extends TypeSafeContentProvider<IWVWMap> {
		public MapObjectivesContentProvider() {
			super(IWVWMap.class);
		}

		@Override
		protected Object[] getTypeSafeElements(final IWVWMap inputElement) {
			if (inputElement != null) {
				return inputElement.getObjectives().toArray(new IWVWObjective[inputElement.getObjectives().size()]);
			} else {
				return super.getTypeSafeElements(inputElement);
			}
		}

		@Override
		public void dispose() {
		}

		@Override
		protected void typeSafeInputChanged(final Viewer viewer, final IWVWMap oldInput, final IWVWMap newInput) {
		}
	}

	public static final String ID = "yagw2api.explorer.rcp.wvw.mapdetails"; //$NON-NLS-1$
	private static final Logger LOGGER = LoggerFactory.getLogger(MapDetailsViewPart.class);
	private static final Function<Optional<IGuild>, String> GUILD_2_STRING = guild -> guild.isPresent() ? "[" + guild.get().getTag() + "] " + guild.get().getName() : "";
	private static final Function<Optional<IWorld>, String> WORLD_2_STRING = world -> world.isPresent() ? world.get().getName().or(String.valueOf(world.get().getId())) : "";
	private static final Function<IWVWObjective, String> OBJECTIVE_2_STRING = objective -> objective.getLabel().or(objective.getType().getLabel());

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
			this.matchSelectionComboViewer.setLabelProvider(new TypeSafeViewerLabelProvider<IWVWMatch>(IWVWMatch.class) {
				@Override
				protected String getTypeSafeText(final IWVWMatch element) {
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
			this.mapSelectionComboViewer.setLabelProvider(new TypeSafeViewerLabelProvider<IWVWMap>(IWVWMap.class) {
				@Override
				protected String getTypeSafeText(final IWVWMap element) {
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
						protected String getTypeSafeText(final IWVWObjective element) {
							return OBJECTIVE_2_STRING.apply(element);
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWObjective>(tableViewerColumn, IWVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final IWVWObjective o) {
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
						protected String getTypeSafeText(final IWVWObjective element) {
							return element.getType().getLabel();
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWObjective>(tableViewerColumn, IWVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final IWVWObjective o) {
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
						protected String getTypeSafeText(final IWVWObjective element) {
							return String.valueOf(element.getType().getPoints());
						}

					});
					new TypeSafeTableViewerColumnSorter<IWVWObjective>(tableViewerColumn, IWVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final IWVWObjective o) {
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
						protected String getTypeSafeText(final IWVWObjective element) {
							return WORLD_2_STRING.apply(element.getOwner());
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWObjective>(tableViewerColumn, IWVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final IWVWObjective o) {
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
						protected String getTypeSafeText(final IWVWObjective element) {
							return GUILD_2_STRING.apply(element.getClaimedByGuild());
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWObjective>(tableViewerColumn, IWVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final IWVWObjective o) {
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
						protected String getTypeSafeText(final IWVWObjective element) {
							return WVWUIConstants.DURATION_FORMAT.apply(Duration.of(element.getRemainingBuffDuration(TimeUnit.SECONDS), ChronoUnit.SECONDS));

						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWObjective>(tableViewerColumn, IWVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final IWVWObjective o) {
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
		if (structuredSelection.getFirstElement() instanceof IWVWMatch) {
			this.selectMatch((IWVWMatch) structuredSelection.getFirstElement());
		} else if (structuredSelection.getFirstElement() instanceof IWVWMap) {
			this.selectMap((IWVWMap) structuredSelection.getFirstElement());
		}
	}

	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		checkNotNull(event, "missing selection  event");
		final IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
		if (structuredSelection.getFirstElement() instanceof IWVWMatch) {
			this.selectMatch((IWVWMatch) structuredSelection.getFirstElement());
		} else if (structuredSelection.getFirstElement() instanceof IWVWMap) {
			this.selectMap((IWVWMap) structuredSelection.getFirstElement());
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

	private Optional<IWVWMap> getSelectedMap() {
		if (this.mapSelectionComboViewer.getSelection().isEmpty()) {
			return Optional.absent();
		} else {
			final IStructuredSelection selection = (IStructuredSelection) this.mapSelectionComboViewer.getSelection();
			checkState(selection.getFirstElement() instanceof IWVWMap, "expected %s to be instance of %s", selection.getFirstElement(), IWVWMap.class);
			return Optional.of((IWVWMap) selection.getFirstElement());
		}
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

	private synchronized void selectMatch(final IWVWMatch match) {
		checkNotNull(match, "missing match");
		final Optional<IWVWMatch> currentMatchSelection = this.getSelectedMatch();
		if (!currentMatchSelection.isPresent() || !currentMatchSelection.get().equals(match)) {
			LOGGER.trace("Select match: {}", match);
			this.matchSelectionComboViewer.setSelection(new StructuredSelection(match));
			this.mapSelectionComboViewer.setInput(match);
		}
		final Optional<IWVWMap> currentMapSelection = this.getSelectedMap();
		if (currentMapSelection.isPresent()) {
			LOGGER.trace("Clear map selection for {}", match);
			this.mapSelectionComboViewer.setSelection(StructuredSelection.EMPTY);
			this.mapObjectivesTableViewer.setInput(null);
			Activator.getDefault().getWVW().unregisterWVWMapListener(this);
		}
	}

	private synchronized void selectMap(final IWVWMap map) {
		checkNotNull(map, "missing map");
		final Optional<IWVWMap> currentMapSelection = this.getSelectedMap();
		if (!currentMapSelection.isPresent() || !currentMapSelection.get().equals(map)) {
			LOGGER.trace("Select map: {}", map);
			final Optional<IWVWMatch> currentMatchSelection = this.getSelectedMatch();
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
	public void onInitializedMatchForWrapper(final IWVWInitializedMatchEvent e) {
		this.refreshUIForMatchUpdate();
	}

	@Override
	public void onMatchScoreChangedEvent(final IWVWMatchScoresChangedEvent e) {
		this.refreshUIForMatchUpdate();
	}

	@Override
	public void onChangedMapScoreEvent(final IWVWMapScoresChangedEvent e) {
		this.refreshUIForMapUpdate();
	}

	@Override
	public void onObjectiveCapturedEvent(final IWVWObjectiveCaptureEvent e) {
		this.refreshUIForMapUpdate();
	}

	@Override
	public void onObjectiveClaimedEvent(final IWVWObjectiveClaimedEvent e) {
		this.refreshUIForMapUpdate();
	}

	@Override
	public void onObjectiveEndOfBuffEvent(final IWVWObjectiveEndOfBuffEvent e) {
		this.refreshUIForMapUpdate();
	}

	@Override
	public void onObjectiveUnclaimedEvent(final IWVWObjectiveUnclaimedEvent e) {
		this.refreshUIForMapUpdate();
	}
}
