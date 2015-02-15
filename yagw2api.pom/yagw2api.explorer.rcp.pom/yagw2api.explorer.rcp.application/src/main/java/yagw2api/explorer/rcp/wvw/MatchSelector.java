package yagw2api.explorer.rcp.wvw;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ColumnPixelData;

public class MatchSelector extends ViewPart {

	public static final String ID = "yagw2api.explorer.rcp.wvw.MatchSelector"; //$NON-NLS-1$
	private Table table;

	public MatchSelector() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		{
			Composite composite = new Composite(container, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			TableColumnLayout tcl_composite = new TableColumnLayout();
			composite.setLayout(tcl_composite);
			{
				TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
				table = tableViewer.getTable();
				table.setHeaderVisible(true);
				table.setLinesVisible(true);
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
					TableColumn tblclmnRedworld = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnRedworld, new ColumnPixelData(150, true, true));
					tblclmnRedworld.setText("Red-World");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
					TableColumn tblclmnGreenworld = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnGreenworld, new ColumnPixelData(150, true, true));
					tblclmnGreenworld.setText("Green-World");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
					TableColumn tblclmnBlueworld = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnBlueworld, new ColumnPixelData(150, true, true));
					tblclmnBlueworld.setText("Blue-World");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
					TableColumn tblclmnRedworldpoints = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnRedworldpoints, new ColumnPixelData(150, true, true));
					tblclmnRedworldpoints.setText("Red-World-Points");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
					TableColumn tblclmnGreenworldpoints = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnGreenworldpoints, new ColumnPixelData(150, true, true));
					tblclmnGreenworldpoints.setText("Green-World-Points");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
					TableColumn tblclmnBlueworldpoints = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnBlueworldpoints, new ColumnPixelData(150, true, true));
					tblclmnBlueworldpoints.setText("Blue-World-Points");
				}
			}
		}

		createActions();
		initializeToolBar();
		initializeMenu();
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
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
