package de.justi.yagw2api.mumblelink.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-MumbleLink
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
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
import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;

import de.justi.yagw2api.mumblelink.IMumbleLink;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatar;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarFrontChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarPositionChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarTopChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraFrontChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraPositionChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraTopChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkMapChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkState;

final class MumbleLink extends AbstractForwardingMumbleLinkState implements IMumbleLink {

	private final MumbleLinkSynchronizerService mumbleLinkUpdater;
	private final Collection<IMumbleLinkListener> listners = new CopyOnWriteArrayList<IMumbleLinkListener>();

	@Override
	public boolean isActive() {
		return this.mumbleLinkUpdater.isRunning();
	}

	@Override
	protected IMumbleLinkState getForwardTo() {
		return this.mumbleLinkUpdater.getCurrentSate().orNull();
	}

	MumbleLink() {
		this.mumbleLinkUpdater = new MumbleLinkSynchronizerService();
		this.mumbleLinkUpdater.getChannel().register(this);
		this.mumbleLinkUpdater.startAsync();
	}

	@Subscribe
	public final void notifyListenersAbout(IMumbleLinkAvatarChangeEvent event) {
		for (IMumbleLinkListener listener : this.listners) {
			listener.onAvatarChange(event);
		}
	}

	@Subscribe
	public final void notifyListenersAbout(IMumbleLinkMapChangeEvent event) {
		for (IMumbleLinkListener listener : this.listners) {
			listener.onMapChange(event);
		}
	}

	@Subscribe
	public final void notifyListenersAbout(IMumbleLinkAvatarPositionChangeEvent event) {
		for (IMumbleLinkListener listener : this.listners) {
			listener.onAvatarPositionChange(event);
		}
	}

	@Subscribe
	public final void notifyListenersAbout(IMumbleLinkAvatarFrontChangeEvent event) {
		for (IMumbleLinkListener listener : this.listners) {
			listener.onAvatarFrontChange(event);
		}
	}

	@Subscribe
	public final void notifyListenersAbout(IMumbleLinkAvatarTopChangeEvent event) {
		for (IMumbleLinkListener listener : this.listners) {
			listener.onAvatarTopChange(event);
		}
	}

	@Subscribe
	public final void notifyListenersAbout(IMumbleLinkCameraPositionChangeEvent event) {
		for (IMumbleLinkListener listener : this.listners) {
			listener.onCameraPositionChange(event);
		}
	}

	@Subscribe
	public final void notifyListenersAbout(IMumbleLinkCameraFrontChangeEvent event) {
		for (IMumbleLinkListener listener : this.listners) {
			listener.onCameraFrontChange(event);
		}
	}

	@Subscribe
	public final void notifyListenersAbout(IMumbleLinkCameraTopChangeEvent event) {
		for (IMumbleLinkListener listener : this.listners) {
			listener.onCameraTopChange(event);
		}
	}

	@Override
	public void setActive(boolean active) {
		if (active != this.isActive()) {
			if (active) {
				this.mumbleLinkUpdater.stopAsync();
			} else {
				this.mumbleLinkUpdater.startAsync();
			}
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("active", this.isActive()).addValue(super.toString()).toString();
	}

	@Override
	public void registerMumbleLinkListener(IMumbleLinkListener listener) {
		checkNotNull(listener);
		checkState(!this.listners.contains(listener));
		this.listners.add(listener);
	}

	@Override
	public void unregisterMumbleLinkListener(IMumbleLinkListener listener) {
		checkNotNull(listener);
		checkState(this.listners.contains(listener));
		this.listners.remove(listener);
	}

}
