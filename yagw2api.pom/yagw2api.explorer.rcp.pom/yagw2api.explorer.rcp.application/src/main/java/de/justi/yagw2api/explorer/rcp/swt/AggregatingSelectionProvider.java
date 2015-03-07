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

import java.util.List;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

import com.google.common.collect.Lists;

public final class AggregatingSelectionProvider implements ISelectionProvider, ISelectionChangedListener {

	private final List<ISelectionChangedListener> listeners = Lists.newCopyOnWriteArrayList();

	private ISelection currentSelection = StructuredSelection.EMPTY;

	@Override
	public void addSelectionChangedListener(final ISelectionChangedListener listener) {
		checkNotNull(listener, "missing listener");
		checkArgument(!listener.equals(this), "detected listener cycle for %s", this);
		this.listeners.add(listener);
	}

	@Override
	public ISelection getSelection() {
		return this.currentSelection;
	}

	@Override
	public void removeSelectionChangedListener(final ISelectionChangedListener listener) {
		checkNotNull(listener, "missing listener");
		this.listeners.remove(listener);
	}

	@Override
	public void setSelection(final ISelection selection) {
		this.currentSelection = selection;
		final SelectionChangedEvent e = new SelectionChangedEvent(this, selection);
		for (final ISelectionChangedListener listener : this.listeners) {
			SafeRunner.run(new SafeRunnable() {
				@Override
				public void run() {
					listener.selectionChanged(e);
				}
			});
		}
	}

	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		checkArgument(!event.getSelectionProvider().equals(this), "detected listener cycle for %s", this);
		this.setSelection(event.getSelection());
	}
}
