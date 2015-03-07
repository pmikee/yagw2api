package de.justi.yagw2api.explorer.rcp.swt;

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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wb.swt.TableViewerColumnSorter;

public class TypeSafeTableViewerColumnSorter<T> extends TableViewerColumnSorter {

	private final Class<T> dataTypeClass;

	public TypeSafeTableViewerColumnSorter(final TableViewerColumn column, final Class<T> dataTypeClass) {
		super(column);
		this.dataTypeClass = checkNotNull(dataTypeClass, "missing datatype class");
	}

	@Override
	protected final int doCompare(final Viewer viewer, final Object e1, final Object e2) {
		checkArgument(this.dataTypeClass.isInstance(e1), "expected %s to be instance of %s", e1, this.dataTypeClass);
		checkArgument(this.dataTypeClass.isInstance(e2), "expected %s to be instance of %s", e2, this.dataTypeClass);
		return this.doCompareTypeSafe(viewer, this.dataTypeClass.cast(e1), this.dataTypeClass.cast(e2));
	}

	@Override
	protected final Object getValue(final Object o) {
		checkArgument(this.dataTypeClass.isInstance(o), "expected %s to be instance of %s", o, this.dataTypeClass);
		return this.getTypeSafeValue(this.dataTypeClass.cast(o));
	}

	protected Object getTypeSafeValue(final T o) {
		return super.getValue(o);
	}

	protected int doCompareTypeSafe(final Viewer viewer, final T e1, final T e2) {
		return super.doCompare(viewer, e1, e2);
	}

}
