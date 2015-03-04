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

import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import yagw2api.explorer.rcp.swt.TypeSafeContentProvider;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWWrapper;

final class MatchesContentProvider extends TypeSafeContentProvider<IWVWWrapper> implements IStructuredContentProvider {
	public MatchesContentProvider() {
		super(IWVWWrapper.class);
	}

	@Override
	public Object[] getTypeSafeElements(final IWVWWrapper inputElement) {
		final Set<IWVWMatch> matches = inputElement.getAllMatches();
		return matches.toArray(new IWVWMatch[matches.size()]);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void typeSafeInputChanged(final Viewer viewer, final IWVWWrapper oldInput, final IWVWWrapper newInput) {
	}
}