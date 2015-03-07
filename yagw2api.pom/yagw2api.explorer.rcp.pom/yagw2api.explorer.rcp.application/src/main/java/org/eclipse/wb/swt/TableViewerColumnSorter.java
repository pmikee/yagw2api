/*******************************************************************************
 * Copyright (c) 2011 Google, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Google, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.wb.swt;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import java.lang.reflect.Method;

import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;

/**
 * Helper for sorting {@link TableViewer} by one of its {@link TableViewerColumn}s.
 * <p>
 * Originally from http://wiki.eclipse.org/index.php/JFaceSnippets, Snippet040TableViewerSorting.
 *
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 * @author Konstantin Scheglov <Konstantin.Scheglov@gmail.com>
 */
public class TableViewerColumnSorter extends ViewerComparator {
	public static final int ASC = 1;
	public static final int NONE = 0;
	public static final int DESC = -1;
	// //////////////////////////////////////////////////////////////////////////
	//
	// Instance fields
	//
	// //////////////////////////////////////////////////////////////////////////
	private final TableViewerColumn m_column;
	private final TableViewer m_viewer;
	private final Table m_table;
	private int m_direction = NONE;

	// //////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	// //////////////////////////////////////////////////////////////////////////
	public TableViewerColumnSorter(final TableViewerColumn column) {
		this.m_column = column;
		this.m_viewer = (TableViewer) column.getViewer();
		this.m_table = this.m_viewer.getTable();
		this.m_column.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (TableViewerColumnSorter.this.m_viewer.getComparator() != null) {
					if (TableViewerColumnSorter.this.m_viewer.getComparator() == TableViewerColumnSorter.this) {
						if (TableViewerColumnSorter.this.m_direction == ASC) {
							TableViewerColumnSorter.this.setSorter(DESC);
						} else if (TableViewerColumnSorter.this.m_direction == DESC) {
							TableViewerColumnSorter.this.setSorter(NONE);
						}
					} else {
						TableViewerColumnSorter.this.setSorter(ASC);
					}
				} else {
					TableViewerColumnSorter.this.setSorter(ASC);
				}
			}
		});
	}

	// //////////////////////////////////////////////////////////////////////////
	//
	// Utils
	//
	// //////////////////////////////////////////////////////////////////////////
	public void setSorter(final int direction) {
		if (direction == NONE) {
			this.m_table.setSortColumn(null);
			this.m_table.setSortDirection(SWT.NONE);
			this.m_viewer.setComparator(null);
		} else {
			this.m_table.setSortColumn(this.m_column.getColumn());
			this.m_direction = direction;
			if (this.m_direction == ASC) {
				this.m_table.setSortDirection(SWT.DOWN);
			} else {
				this.m_table.setSortDirection(SWT.UP);
			}
			if (this.m_viewer.getComparator() == this) {
				this.m_viewer.refresh();
			} else {
				this.m_viewer.setComparator(this);
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	//
	// ViewerComparator
	//
	// //////////////////////////////////////////////////////////////////////////
	@Override
	public int compare(final Viewer viewer, final Object e1, final Object e2) {
		return this.m_direction * this.doCompare(viewer, e1, e2);
	}

	/**
	 * Compares to elements of viewer. By default tries to compare values extracted from these elements using {@link #getValue(Object)}, because usually you want to compare value
	 * of some attribute.
	 */
	@SuppressWarnings("unchecked")
	protected int doCompare(final Viewer viewer, final Object e1, final Object e2) {
		Object o1 = this.getValue(e1);
		Object o2 = this.getValue(e2);
		if (o1 instanceof Comparable && o2 instanceof Comparable) {
			return ((Comparable) o1).compareTo(o2);
		}
		return 0;
	}

	/**
	 *
	 * @return the value to compare in {@link #doCompare(Viewer, Object, Object)}. Be default tries to get it from {@link EditingSupport}. May return <code>null</code>.
	 */
	protected Object getValue(final Object o) {
		try {
			EditingSupport editingSupport;
			{
				Method getEditingMethod = ViewerColumn.class.getDeclaredMethod("getEditingSupport", new Class[] {});
				getEditingMethod.setAccessible(true);
				editingSupport = (EditingSupport) getEditingMethod.invoke(this.m_column, new Object[] {});
			}
			if (editingSupport != null) {
				Method getValueMethod = EditingSupport.class.getDeclaredMethod("getValue", new Class[] { Object.class });
				getValueMethod.setAccessible(true);
				return getValueMethod.invoke(editingSupport, new Object[] { o });
			}
		} catch (Throwable e) {
		}
		return null;
	}
}
