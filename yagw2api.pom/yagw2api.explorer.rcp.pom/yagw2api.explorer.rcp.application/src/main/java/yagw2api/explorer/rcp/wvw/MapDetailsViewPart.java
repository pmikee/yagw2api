package yagw2api.explorer.rcp.wvw;

import static com.google.common.base.Preconditions.checkNotNull;

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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yagw2api.explorer.rcp.Activator;
import yagw2api.explorer.rcp.swt.TypeSafeContentProvider;
import yagw2api.explorer.rcp.swt.TypeSafeTableViewerColumnSorter;
import yagw2api.explorer.rcp.swt.TypeSafeViewerLabelProvider;
import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchListener;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWObjective;

public class MapDetailsViewPart extends ViewPart implements ISelectionListener, ISelectionChangedListener, IWVWMatchListener {
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

	private Table mapObjectivesTable;
	private TableViewer mapObjectivesTableViewer;
	private ComboViewer matchSelectionComboViewer;
	private ComboViewer mapSelectionComboViewer;
	private Combo matchSelectionCombo;
	private Combo mapSelectionCombo;

	public MapDetailsViewPart() {
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
			this.matchSelectionComboViewer.addSelectionChangedListener(this);
		}
		{
			CLabel lblMap = new CLabel(container, SWT.NONE);
			lblMap.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			lblMap.setText("Map");
		}
		{
			this.mapSelectionComboViewer = new ComboViewer(container, SWT.READ_ONLY);
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
							return element.getLabel().or(element.getType().getLabel());
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWObjective>(tableViewerColumn, IWVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final IWVWObjective o) {
							return o.getLabel().or(o.getType().getLabel());
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
							if (element.getOwner().isPresent()) {
								return element.getOwner().get().getName().or(String.valueOf(element.getOwner().get().getId()));
							} else {
								return "";
							}
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWObjective>(tableViewerColumn, IWVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final IWVWObjective o) {
							if (o.getOwner().isPresent()) {
								return o.getOwner().get().getName().or(String.valueOf(o.getOwner().get().getId()));
							} else {
								return "";
							}
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
							if (element.getClaimedByGuild().isPresent()) {
								return element.getClaimedByGuild().get().getName();
							} else {
								return "";
							}
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWObjective>(tableViewerColumn, IWVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final IWVWObjective o) {
							if (o.getClaimedByGuild().isPresent()) {
								return o.getClaimedByGuild().get().getName();
							} else {
								return "";
							}
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
							if (element.getClaimedByGuild().isPresent()) {
								return String.valueOf(element.getRemainingBuffDuration(TimeUnit.SECONDS));
							} else {
								return "";
							}
						}
					});
					new TypeSafeTableViewerColumnSorter<IWVWObjective>(tableViewerColumn, IWVWObjective.class) {
						@Override
						protected Object getTypeSafeValue(final IWVWObjective o) {
							if (o.getClaimedByGuild().isPresent()) {
								return String.valueOf(o.getRemainingBuffDuration(TimeUnit.SECONDS));
							} else {
								return "";
							}
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

		this.matchSelectionComboViewer.setInput(Activator.getDefault().getWVW());
		this.getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
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
			LOGGER.trace("Selected {} in {}", structuredSelection, part);
			this.mapSelectionComboViewer.setInput(structuredSelection.getFirstElement());
			this.mapSelectionComboViewer.setSelection(StructuredSelection.EMPTY);
			this.matchSelectionComboViewer.setSelection(structuredSelection);
		} else if (structuredSelection.getFirstElement() instanceof IWVWMap) {
			LOGGER.trace("Selected {} in {}", structuredSelection, part);
			this.mapObjectivesTableViewer.setInput(structuredSelection.getFirstElement());
			this.mapSelectionComboViewer.setSelection(structuredSelection);
		}
	}

	/**
	 * refreshes the match selection combo(viewer)
	 */
	private void refreshUI() {
		Display.getDefault().asyncExec(() -> {
			if (this.matchSelectionCombo != null && !this.matchSelectionCombo.isDisposed()) {
				this.matchSelectionComboViewer.refresh();
			} else {
				LOGGER.warn("can't refresh match selection combo");
			}
		});
	}

	@Override
	public void onInitializedMatchForWrapper(final IWVWInitializedMatchEvent e) {
		this.refreshUI();
	}

	@Override
	public void onMatchScoreChangedEvent(final IWVWMatchScoresChangedEvent e) {
		this.refreshUI();
	}

	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		checkNotNull(event, "missing selection  event");
		final IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
		if (structuredSelection.getFirstElement() instanceof IWVWMatch) {
			LOGGER.trace("Selected {}", structuredSelection);
			this.mapSelectionComboViewer.setInput(structuredSelection.getFirstElement());
			this.mapSelectionComboViewer.setSelection(StructuredSelection.EMPTY);
			this.mapObjectivesTableViewer.setInput(null);
		} else if (structuredSelection.getFirstElement() instanceof IWVWMap) {
			LOGGER.trace("Selected {}", structuredSelection);
			this.mapObjectivesTableViewer.setInput(structuredSelection.getFirstElement());
		}
	}

}
