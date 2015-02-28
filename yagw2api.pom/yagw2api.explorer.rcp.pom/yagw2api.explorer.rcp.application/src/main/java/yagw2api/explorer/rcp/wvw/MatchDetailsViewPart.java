package yagw2api.explorer.rcp.wvw;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yagw2api.explorer.rcp.Activator;
import yagw2api.explorer.rcp.swt.TypeSafeColumnLabelProvider;
import yagw2api.explorer.rcp.swt.TypeSafeContentProvider;
import yagw2api.explorer.rcp.swt.TypeSafeTableViewerColumnSorter;
import yagw2api.explorer.rcp.swt.TypeSafeViewerLabelProvider;
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

	private final MatchesContentProvider contentProvider = new MatchesContentProvider();
	private ComboViewer matchSelectionComboViewer;
	private Combo matchSelectionCombo;
	private Table mapsTable;
	private TableViewer mapsTableViewer;

	public MatchDetailsViewPart() {
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
			this.matchSelectionComboViewer.addSelectionChangedListener(this);
			this.matchSelectionCombo = this.matchSelectionComboViewer.getCombo();
			this.matchSelectionCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
			{
				Composite composite = new Composite(container, SWT.NONE);
				composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
				TableColumnLayout tcl_composite = new TableColumnLayout();
				composite.setLayout(tcl_composite);
				{
					this.mapsTableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
					this.getSite().setSelectionProvider(this.mapsTableViewer);
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
						tableViewerColumn.setLabelProvider(new RedWorldColumnLabelProvider<IWVWMap>(IWVWMap.class) {
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
						tableViewerColumn.setLabelProvider(new GreenWorldColumnLabelProvider<IWVWMap>(IWVWMap.class) {
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
						tableViewerColumn.setLabelProvider(new BlueWorldColumnLabelProvider<IWVWMap>(IWVWMap.class) {
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
						tableViewerColumn.setLabelProvider(new RedWorldColumnLabelProvider<IWVWMap>(IWVWMap.class) {
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
						tableViewerColumn.setLabelProvider(new GreenWorldColumnLabelProvider<IWVWMap>(IWVWMap.class) {
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
						tableViewerColumn.setLabelProvider(new BlueWorldColumnLabelProvider<IWVWMap>(IWVWMap.class) {
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
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		// handle selection events from workbench
		checkNotNull(part, "missing part");
		checkNotNull(selection, "missing selection");
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.getFirstElement() instanceof IWVWMatch) {
			LOGGER.trace("Selected {} in {}", selection, part);
			if (this.matchSelectionComboViewer != null) {
				this.matchSelectionComboViewer.setSelection(selection);
				this.selectTableData(structuredSelection);
			}
		}
	}

	/**
	 * replaces current table content with given {@code selection} if it contains a {@link IWVWMatch} as it's first element
	 *
	 * @param selection
	 *            to replace table content with
	 */
	private void selectTableData(final IStructuredSelection selection) {
		checkNotNull(selection, "missing selection");
		if (!selection.isEmpty()) {
			if (selection.getFirstElement() instanceof IWVWMatch) {
				this.mapsTableViewer.setInput(selection.getFirstElement());
			}
		}
	}

	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		// handle selection of views combo box
		checkNotNull(event, "missing event");
		final IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
		this.selectTableData(structuredSelection);
	}

	@Override
	public void dispose() {
		Activator.getDefault().getWVW().unregisterWVWMatchListener(this);
	}

}
