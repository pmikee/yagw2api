package yagw2api.explorer.rcp.wvw;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import yagw2api.explorer.rcp.swt.TypeSafeColumnLabelProvider;
import yagw2api.explorer.rcp.swt.TypeSafeViewerLabelProvider;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchListener;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;

public class MatchDetailsViewPart extends ViewPart implements ISelectionListener, ISelectionChangedListener, IWVWMatchListener {
	private static class ContentProvider implements IStructuredContentProvider {
		private Optional<IWVWMatch> currentInput = Optional.absent();

		private final TableColumn redTickColumn;
		private final TableColumn greenTickColumn;
		private final TableColumn blueTickColumn;

		public ContentProvider(final TableColumn redTickColumn, final TableColumn greenTickColumn, final TableColumn blueTickColumn) {
			this.redTickColumn = checkNotNull(redTickColumn, "missing red tick column");
			this.greenTickColumn = checkNotNull(greenTickColumn, "missing green tick column");
			this.blueTickColumn = checkNotNull(blueTickColumn, "missing blue tick column");
		}

		@Override
		public Object[] getElements(final Object inputElement) {
			return this.currentInput.isPresent() ? new IWVWMap[] { this.currentInput.get().getRedMap(), this.currentInput.get().getGreenMap(),
					this.currentInput.get().getBlueMap(), this.currentInput.get().getCenterMap() } : new Object[0];
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			if (newInput != null) {
				final IWVWMatch match = (IWVWMatch) newInput;
				this.currentInput = Optional.of(match);
				this.redTickColumn.setText(match.getRedWorld().getName().or("Red-Tick"));
				this.greenTickColumn.setText(match.getGreenWorld().getName().or("Green-Tick"));
				this.blueTickColumn.setText(match.getBlueWorld().getName().or("Blue-Tick"));
			} else {
				this.currentInput = Optional.absent();
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
			this.matchSelectionCombo = this.matchSelectionComboViewer.getCombo();
			this.matchSelectionCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
			{
				Composite composite = new Composite(container, SWT.NONE);
				composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
				TableColumnLayout tcl_composite = new TableColumnLayout();
				composite.setLayout(tcl_composite);
				{
					this.mapsTableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
					this.mapsTable = this.mapsTableViewer.getTable();
					this.mapsTable.setHeaderVisible(true);
					this.mapsTable.setLinesVisible(true);
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new TypeSafeColumnLabelProvider<IWVWMap>(IWVWMap.class) {
							@Override
							public String getTypeSafeText(final IWVWMap element) {
								return element.getType().toString();
							}
						});
						TableColumn tblclmnMap = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnMap, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnMap.setText("Map");
					}
					TableColumn tblclmnRedtick;
					TableColumn tblclmnGreentick;
					TableColumn tblclmnBluetick;
					{
						TableViewerColumn tableViewerColumn = new TableViewerColumn(this.mapsTableViewer, SWT.NONE);
						tableViewerColumn.setLabelProvider(new RedWorldColumnLabelProvider<IWVWMap>(IWVWMap.class) {
							@Override
							public String getTypeSafeText(final IWVWMap element) {
								return WVWUIConstants.NUMBER_FORMAT_POINTS.format(element.calculateRedTick());
							}
						});
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
						tblclmnBluetick = tableViewerColumn.getColumn();
						tcl_composite.setColumnData(tblclmnBluetick, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
						tblclmnBluetick.setText("Blue-Tick");
					}
					this.mapsTableViewer.setContentProvider(new ContentProvider(tblclmnRedtick, tblclmnGreentick, tblclmnBluetick));
				}
			}
			this.matchSelectionComboViewer.setLabelProvider(new TypeSafeViewerLabelProvider<IWVWMatch>(IWVWMatch.class) {
				@Override
				protected String getTypeSafeText(final IWVWMatch element) {
					return element.getRedWorld().getWorldLocation() + ": " + element.getRedWorld().getName().get() + " vs. " + element.getGreenWorld().getName().get() + " vs. "
							+ element.getBlueWorld().getName().get();
				}
			});
			this.matchSelectionComboViewer.setContentProvider(this.contentProvider);
			this.matchSelectionComboViewer.addSelectionChangedListener(this);
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

	private void refreshTable() {
		if (this.matchSelectionComboViewer != null) {
			Display.getDefault().asyncExec(() -> {
				if (!this.matchSelectionCombo.isDisposed()) {
					this.matchSelectionComboViewer.refresh();
				}
			});
		}
	}

	@Override
	public void onInitializedMatchForWrapper(final IWVWInitializedMatchEvent arg0) {
		this.refreshTable();
	}

	@Override
	public void onMatchScoreChangedEvent(final IWVWMatchScoresChangedEvent arg0) {
		this.refreshTable();
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		LOGGER.info("Selected {} in {}", selection, part);
		if (this.matchSelectionComboViewer != null) {
			this.matchSelectionComboViewer.setSelection(selection);
			this.selectTableData(selection);
		}
	}

	private void selectTableData(final ISelection selection) {
		if (!selection.isEmpty()) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.getFirstElement() instanceof IWVWMatch) {
				this.mapsTableViewer.setInput(structuredSelection.getFirstElement());
			}
		}
	}

	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		this.selectTableData(event.getSelection());
	}

	@Override
	public void dispose() {
		Activator.getDefault().getWVW().unregisterWVWMatchListener(this);
	}

}
