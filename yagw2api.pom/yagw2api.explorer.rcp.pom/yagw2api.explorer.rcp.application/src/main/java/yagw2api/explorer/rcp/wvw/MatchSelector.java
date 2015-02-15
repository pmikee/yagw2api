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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import yagw2api.explorer.rcp.Activator;

import com.google.common.collect.Lists;

import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchListener;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWWrapper;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class MatchSelector extends ViewPart implements IWVWMatchListener {
	private static final NumberFormat NF = new DecimalFormat("#,###,##0");
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		public String getColumnText(Object element, int columnIndex) {
			checkArgument(element instanceof IWVWMatch);
			final IWVWMatch match = (IWVWMatch)element;
			switch (columnIndex) {
				case 0:
					return (match.getRedWorld().getName().get());
				case 1:
					return (match.getGreenWorld().getName().get());
				case 2:
					return (match.getBlueWorld().getName().get());
				case 3:
					return NF.format(match.getScores().getGreenScore());
				case 4:
					return NF.format(match.getScores().getBlueScore());
				case 5:
					return NF.format(match.getScores().getRedScore());
				default:
					throw new IllegalArgumentException("Unknown column: " + columnIndex);
			}
		}
	}
	private static final class ContentProvider implements IStructuredContentProvider, IWVWMatchListener {

		private final List<IWVWMatch> matches = Collections.synchronizedList(Lists.newCopyOnWriteArrayList());
		
		public Object[] getElements(Object inputElement) {
			return matches.toArray(new IWVWMatch[this.matches.size()]);
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.matches.clear();
			if (oldInput != null) {
				checkArgument(oldInput instanceof IWVWWrapper);
				((IWVWWrapper) oldInput).unregisterWVWMatchListener(this);
			}
			if (newInput != null) {
				checkArgument(newInput instanceof IWVWWrapper);
				((IWVWWrapper) newInput).registerWVWMatchListener(this);
			}
		}

		@Override
		public void onInitializedMatchForWrapper(IWVWInitializedMatchEvent e) {
			this.matches.add(e.getMatch());
			
		}

		@Override
		public void onMatchScoreChangedEvent(IWVWMatchScoresChangedEvent e) {
			// NOP
		}
	}

	public static final String ID = "yagw2api.explorer.rcp.wvw.MatchSelector"; //$NON-NLS-1$
	private final ContentProvider contentProvider = new ContentProvider();
	private Table table = null;
	private TableViewer tableViewer = null;

	public MatchSelector() {	
		Activator.getDefault().getWVW().registerWVWMatchListener(this);
	}

	/**
	 * Create contents of the view part.
	 * 
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
				tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
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
				tableViewer.setLabelProvider(new TableLabelProvider());
				tableViewer.setContentProvider(this.contentProvider);
			}
		}

		createActions();
		initializeToolBar();
		initializeMenu();
		
		tableViewer.setInput(Activator.getDefault().getWVW());
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

	@Override
	public void onInitializedMatchForWrapper(IWVWInitializedMatchEvent arg0) {
		if(this.tableViewer != null){
			Display.getDefault().asyncExec(() -> {
				this.tableViewer.refresh();	
			});
		}
	}

	@Override
	public void onMatchScoreChangedEvent(IWVWMatchScoresChangedEvent arg0) {
		if(this.tableViewer != null){
			Display.getDefault().asyncExec(() -> {
				this.tableViewer.refresh();	
			});
		}
	}

}
