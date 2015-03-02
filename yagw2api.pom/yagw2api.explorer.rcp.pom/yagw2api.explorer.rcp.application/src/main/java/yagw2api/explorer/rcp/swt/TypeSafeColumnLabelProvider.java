package yagw2api.explorer.rcp.swt;

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

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public class TypeSafeColumnLabelProvider<T> extends ColumnLabelProvider {
	private final Class<T> dataTypeClass;

	public TypeSafeColumnLabelProvider(final Class<T> dataTypeClass) {
		this.dataTypeClass = checkNotNull(dataTypeClass, "missing datatype class");
	}

	@Override
	public final Font getFont(final Object element) {
		checkArgument(this.dataTypeClass.isInstance(element), "expected %s to be instance of %s", element, this.dataTypeClass);
		return this.getTypeSafeFont(this.dataTypeClass.cast(element));
	}

	@Override
	public final Color getBackground(final Object element) {
		checkArgument(this.dataTypeClass.isInstance(element), "expected %s to be instance of %s", element, this.dataTypeClass);
		return this.getTypeSafeBackground(this.dataTypeClass.cast(element));
	}

	@Override
	public final Color getForeground(final Object element) {
		checkArgument(this.dataTypeClass.isInstance(element), "expected %s to be instance of %s", element, this.dataTypeClass);
		return this.getTypeSafeForeground(this.dataTypeClass.cast(element));
	}

	@Override
	public final Image getImage(final Object element) {
		checkArgument(this.dataTypeClass.isInstance(element), "expected %s to be instance of %s", element, this.dataTypeClass);
		return this.getTypeSafeImage(this.dataTypeClass.cast(element));
	}

	@Override
	public final String getText(final Object element) {
		checkArgument(this.dataTypeClass.isInstance(element), "expected %s to be instance of %s", element, this.dataTypeClass);
		return this.getTypeSafeText(this.dataTypeClass.cast(element));
	}

	protected String getTypeSafeText(final T element) {
		return super.getText(element);
	}

	protected Image getTypeSafeImage(final T element) {
		return super.getImage(element);
	}

	protected Font getTypeSafeFont(final T element) {
		return super.getFont(element);
	}

	protected Color getTypeSafeBackground(final T element) {
		return super.getBackground(element);
	}

	protected Color getTypeSafeForeground(final T element) {
		return super.getForeground(element);
	}
}