package de.justi.yagw2api.explorer.rcp.wvw;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * yagw2api.explorer.rcp.application.wvw
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

import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.justi.yagw2api.explorer.rcp.swt.TypeSafeContentProvider;
import de.justi.yagw2api.wrapper.domain.wvw.WVWMatch;
import de.justi.yagw2api.wrapper.wvw.WVWWrapper;

final class MatchesContentProvider extends TypeSafeContentProvider<WVWWrapper> implements IStructuredContentProvider {
	public MatchesContentProvider() {
		super(WVWWrapper.class);
	}

	@Override
	public Object[] getTypeSafeElements(final WVWWrapper inputElement) {
		final Set<WVWMatch> matches = inputElement.getAllMatches();
		return matches.toArray(new WVWMatch[matches.size()]);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void typeSafeInputChanged(final Viewer viewer, final WVWWrapper oldInput, final WVWWrapper newInput) {
	}
}
