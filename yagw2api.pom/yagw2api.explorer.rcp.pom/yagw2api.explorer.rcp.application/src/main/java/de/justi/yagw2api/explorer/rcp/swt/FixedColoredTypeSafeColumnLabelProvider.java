package de.justi.yagw2api.explorer.rcp.swt;

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

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.wb.swt.SWTResourceManager;

public class FixedColoredTypeSafeColumnLabelProvider<T> extends TypeSafeColumnLabelProvider<T> {

	private final Color backgroundColor;
	private final Color foregroundColor;

	public FixedColoredTypeSafeColumnLabelProvider(final Class<T> dataTypeClass, final RGB background, final RGB foreground) {
		super(checkNotNull(dataTypeClass, "missing data type class"));
		this.backgroundColor = SWTResourceManager.getColor(checkNotNull(background, "missing background"));
		this.foregroundColor = SWTResourceManager.getColor(checkNotNull(foreground, "missing foreground"));
	}

	@Override
	protected Color getTypeSafeBackground(final T element) {
		return this.backgroundColor;
	}

	@Override
	protected Color getTypeSafeForeground(final T element) {
		return this.foregroundColor;
	}
}
