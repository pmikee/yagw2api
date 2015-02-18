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

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import yagw2api.explorer.rcp.Activator;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchListener;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWWrapper;

public class MatchSelector extends ViewPart implements IWVWMatchListener {

	private static class TableLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {
		@Override
		public Image getColumnImage(final Object element, final int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(final Object element, final int columnIndex) {
			checkArgument(element instanceof IWVWMatch);
			final IWVWMatch match = (IWVWMatch) element;
			switch (columnIndex) {
				case 0:
					return (match.getRedWorld().getName().get());
				case 1:
					return (match.getGreenWorld().getName().get());
				case 2:
					return (match.getBlueWorld().getName().get());
				case 3:
					return WVWUIConstants.NUMBER_FORMAT_POINTS.format(match.getScores().getRedScore());
				case 4:
					return WVWUIConstants.NUMBER_FORMAT_POINTS.format(match.getScores().getGreenScore());
				case 5:
					return WVWUIConstants.NUMBER_FORMAT_POINTS.format(match.getScores().getBlueScore());
				default:
					return WVWUIConstants.LABEL_NO_SUCH_DATA;
			}
		}

		@Override
		public Color getForeground(final Object element, final int columnIndex) {
			switch (columnIndex) {
				case 0:
				case 3:
					return SWTResourceManager.getColor(WVWUIConstants.RGB_RED_WORLD_FG);
				case 1:
				case 4:
					return SWTResourceManager.getColor(WVWUIConstants.RGB_GREEN_WORLD_FG);
				case 2:
				case 5:
					return SWTResourceManager.getColor(WVWUIConstants.RGB_BLUE_WORLD_FG);
				default:
					return null;
			}
		}

		@Override
		public Color getBackground(final Object element, final int columnIndex) {
			switch (columnIndex) {
				case 0:
				case 3:
					return SWTResourceManager.getColor(WVWUIConstants.RGB_RED_WORLD_BG);
				case 1:
				case 4:
					return SWTResourceManager.getColor(WVWUIConstants.RGB_GREEN_WORLD_BG);
				case 2:
				case 5:
					return SWTResourceManager.getColor(WVWUIConstants.RGB_BLUE_WORLD_BG);
				default:
					return null;
			}
		}
	}

	private static final class ContentProvider implements IStructuredContentProvider, IWVWMatchListener {
		private Optional<IWVWWrapper> currentInput = Optional.absent();
		private final List<IWVWMatch> matches = Collections.synchronizedList(Lists.newCopyOnWriteArrayList());

		@Override
		public Object[] getElements(final Object inputElement) {
			return this.matches.toArray(new IWVWMatch[this.matches.size()]);
		}

		@Override
		public void dispose() {
			if (this.currentInput.isPresent()) {
				this.currentInput.get().unregisterWVWMatchListener(this);
			}
		}

		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			this.matches.clear();
			if (oldInput != null) {
				checkArgument(oldInput instanceof IWVWWrapper);
				((IWVWWrapper) oldInput).unregisterWVWMatchListener(this);
			}
			if (newInput != null) {
				checkArgument(newInput instanceof IWVWWrapper);
				this.currentInput = Optional.of(((IWVWWrapper) newInput));
				this.currentInput.get().registerWVWMatchListener(this);
			}
		}

		@Override
		public void onInitializedMatchForWrapper(final IWVWInitializedMatchEvent e) {
			this.matches.add(e.getMatch());

		}

		@Override
		public void onMatchScoreChangedEvent(final IWVWMatchScoresChangedEvent e) {
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
				this.tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
				this.table = this.tableViewer.getTable();
				this.table.setHeaderVisible(true);
				this.table.setLinesVisible(true);
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
					TableColumn tblclmnRedworld = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnRedworld, new ColumnWeightData(1, true));
					tblclmnRedworld.setText("Red-World");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
					TableColumn tblclmnGreenworld = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnGreenworld, new ColumnWeightData(1, true));
					tblclmnGreenworld.setText("Green-World");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
					TableColumn tblclmnBlueworld = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnBlueworld, new ColumnWeightData(1, true));
					tblclmnBlueworld.setText("Blue-World");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
					TableColumn tblclmnRedworldpoints = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnRedworldpoints, new ColumnWeightData(1, true));
					tblclmnRedworldpoints.setText("Red-World-Points");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
					TableColumn tblclmnGreenworldpoints = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnGreenworldpoints, new ColumnWeightData(1, true));
					tblclmnGreenworldpoints.setText("Green-World-Points");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
					TableColumn tblclmnBlueworldpoints = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnBlueworldpoints, new ColumnWeightData(1, true));
					tblclmnBlueworldpoints.setText("Blue-World-Points");
				}
				this.tableViewer.setLabelProvider(new TableLabelProvider());
				this.tableViewer.setContentProvider(this.contentProvider);
			}
		}

		this.createActions();
		this.initializeToolBar();
		this.initializeMenu();

		this.tableViewer.setInput(Activator.getDefault().getWVW());
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
		if (this.tableViewer != null) {
			Display.getDefault().asyncExec(() -> {
				if (!this.table.isDisposed()) {
					this.tableViewer.refresh();
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
	public void dispose() {
		Activator.getDefault().getWVW().unregisterWVWMatchListener(this);
	}

}
