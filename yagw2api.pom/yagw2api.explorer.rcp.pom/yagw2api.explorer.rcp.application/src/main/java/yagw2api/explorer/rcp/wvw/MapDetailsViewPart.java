package yagw2api.explorer.rcp.wvw;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapDetailsViewPart extends ViewPart implements ISelectionListener {

	public static final String ID = "yagw2api.explorer.rcp.wvw.mapdetails"; //$NON-NLS-1$
	private static final Logger LOGGER = LoggerFactory.getLogger(MapDetailsViewPart.class);

	private Table table;

	public MapDetailsViewPart() {
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
			ListViewer listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
			List list = listViewer.getList();
			list.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		}
		{
			Composite composite = new Composite(container, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			composite.setLayout(new TableColumnLayout());
			{
				TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
				this.table = tableViewer.getTable();
				this.table.setHeaderVisible(true);
				this.table.setLinesVisible(true);
			}
		}

		this.createActions();
		this.initializeToolBar();
		this.initializeMenu();

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
		LOGGER.trace("Selected {} in {}", selection, part);
	}

}
