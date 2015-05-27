package de.justi.yagw2api.mumblelink.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-MumbleLink
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

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;

import de.justi.yagw2api.mumblelink.IMumbleLinkState;
import de.justi.yagw2api.mumblelink.MumleLinkConstants;
import de.justi.yagwapi.common.event.HasChannel;

final class MumbleLinkSynchronizerService extends AbstractScheduledService implements HasChannel {
	private static final int INITIAL_UI_TICK_VALUE = -1;
	private static final int DELAY_MILLIS = 1000;
	private static final Logger LOGGER = LoggerFactory.getLogger(MumbleLinkSynchronizerService.class);
	private volatile Pointer sharedMemory = null;
	private volatile HANDLE sharedFile = null;

	private EventBus channel = new EventBus(this.getClass().getName());

	private volatile Optional<IMumbleLinkState> lastState = Optional.absent();
	private volatile Optional<IMumbleLinkState> currentState = Optional.absent();
	private int lastUITick = INITIAL_UI_TICK_VALUE;

	@Override
	protected Scheduler scheduler() {
		return AbstractScheduledService.Scheduler.newFixedDelaySchedule(0, DELAY_MILLIS, TimeUnit.MILLISECONDS);
	}

	public final Optional<IMumbleLinkState> getCurrentSate() {
		return this.currentState;
	}

	@Override
	protected void runOneIteration() throws Exception {
		try {
			if (this.sharedFile == null) {
				synchronized (this) {
					if (this.sharedFile == null) {
						this.sharedFile = Kernel32.INSTANCE.CreateFileMapping(WinBase.INVALID_HANDLE_VALUE, null, WinNT.PAGE_EXECUTE_READWRITE, 0, MumleLinkConstants.MEM_MAP_SIZE,
								MumleLinkConstants.MEM_MAP_NAME);
					}
				}
			}
			if ((this.sharedFile != null) && (this.sharedMemory == null)) {
				synchronized (this) {
					if (this.sharedMemory == null) {
						this.sharedMemory = Kernel32.INSTANCE.MapViewOfFile(this.sharedFile, WinNT.SECTION_MAP_READ, 0, 0, MumleLinkConstants.MEM_MAP_SIZE);
					}
				}
			}

			synchronized (this) {
				this.lastState = this.currentState;
				if (this.sharedMemory != null) {

					final Optional<IMumbleLinkState> newState = Optional.fromNullable(MumbleLinkState.of(this.sharedMemory));
					if (!newState.isPresent() || (newState.get().getUITick().isPresent() && (newState.get().getUITick().get().intValue() != this.lastUITick))) {
						this.currentState = newState;
						this.lastUITick = this.currentState.isPresent() ? this.currentState.get().getUITick().get() : this.lastUITick;
					} else {
						this.currentState = Optional.absent();
					}
				} else {
					this.currentState = Optional.absent();
				}
			}
			if (!this.lastState.equals(this.currentState)) {
				// dirty state
				// -> create position events where required
				if (this.currentState.isPresent() && this.lastState.isPresent()) {
					// current and last state are present -> create selective events
					LOGGER.trace(this + " synchronized state with game.");
					if (!this.currentState.get().getAvatar().equals(this.lastState.get().getAvatar())) {
						this.getChannel().post(new MumbleLinkAvatarChangeEvent(this.lastState.get().getAvatar().orNull(), this.currentState.get().getAvatar().orNull()));
					}
					if (!this.currentState.get().getMapId().equals(this.lastState.get().getMapId())) {
						this.getChannel().post(new MumbleLinkMapChangeEvent(this.lastState.get().getMapId().get(), this.currentState.get().getMapId().get()));
					}
					if (!this.currentState.get().getAvatarFront().equals(this.lastState.get().getAvatarFront())) {
						this.getChannel().post(new MumbleLinkAvatarFrontChangeEvent(this.lastState.get().getAvatarFront().get(), this.currentState.get().getAvatarFront().get()));
					}
					if (!this.currentState.get().getAvatarTop().equals(this.lastState.get().getAvatarTop())) {
						this.getChannel().post(new MumbleLinkAvatarTopChangeEvent(this.lastState.get().getAvatarTop().get(), this.currentState.get().getAvatarTop().get()));
					}
					if (!this.currentState.get().getAvatarPosition().equals(this.lastState.get().getAvatarPosition())) {
						this.getChannel().post(
								new MumbleLinkAvatarPositionChangeEvent(this.lastState.get().getAvatarPosition().get(), this.currentState.get().getAvatarPosition().get()));
					}
					if (!this.currentState.get().getCameraFront().equals(this.lastState.get().getCameraFront())) {
						this.getChannel().post(new MumbleLinkCameraFrontChangeEvent(this.lastState.get().getCameraFront().get(), this.currentState.get().getCameraFront().get()));
					}
					if (!this.currentState.get().getCameraTop().equals(this.lastState.get().getCameraTop())) {
						this.getChannel().post(new MumbleLinkCameraTopChangeEvent(this.lastState.get().getCameraTop().get(), this.currentState.get().getCameraTop().get()));
					}
					if (!this.currentState.get().getCameraPosition().equals(this.lastState.get().getCameraPosition())) {
						this.getChannel().post(
								new MumbleLinkCameraPositionChangeEvent(this.lastState.get().getCameraPosition().get(), this.currentState.get().getCameraPosition().get()));
					}
				} else if (this.currentState.isPresent()) {
					// now value present til now -> create all events
					LOGGER.info(this + " established connection to game.");
					this.getChannel().post(new MumbleLinkAvatarChangeEvent(null, this.currentState.get().getAvatar().orNull()));
					this.getChannel().post(new MumbleLinkMapChangeEvent(null, this.currentState.get().getMapId().get()));

					this.getChannel().post(new MumbleLinkAvatarFrontChangeEvent(null, this.currentState.get().getAvatarFront().get()));
					this.getChannel().post(new MumbleLinkAvatarTopChangeEvent(null, this.currentState.get().getAvatarTop().get()));
					this.getChannel().post(new MumbleLinkAvatarPositionChangeEvent(null, this.currentState.get().getAvatarPosition().get()));
					this.getChannel().post(new MumbleLinkCameraFrontChangeEvent(null, this.currentState.get().getCameraFront().get()));
					this.getChannel().post(new MumbleLinkCameraTopChangeEvent(null, this.currentState.get().getCameraTop().get()));
					this.getChannel().post(new MumbleLinkCameraPositionChangeEvent(null, this.currentState.get().getCameraPosition().get()));
				} else if (this.lastState.isPresent()) {
					// new state is absent -> create all events
					LOGGER.warn(this + " lost connection to game.");
					this.getChannel().post(new MumbleLinkAvatarChangeEvent(this.lastState.get().getAvatar().orNull(), null));
					this.getChannel().post(new MumbleLinkMapChangeEvent(this.lastState.get().getMapId().get(), null));

					this.getChannel().post(new MumbleLinkAvatarFrontChangeEvent(this.lastState.get().getAvatarFront().get(), null));
					this.getChannel().post(new MumbleLinkAvatarTopChangeEvent(this.lastState.get().getAvatarTop().get(), null));
					this.getChannel().post(new MumbleLinkAvatarPositionChangeEvent(this.lastState.get().getAvatarPosition().get(), null));
					this.getChannel().post(new MumbleLinkCameraFrontChangeEvent(this.lastState.get().getCameraFront().get(), null));
					this.getChannel().post(new MumbleLinkCameraTopChangeEvent(this.lastState.get().getCameraTop().get(), null));
					this.getChannel().post(new MumbleLinkCameraPositionChangeEvent(this.lastState.get().getCameraPosition().get(), null));
				}
			} else if (this.currentState.isPresent()) {
				// non dirty state but lost connectivity
				LOGGER.warn(this + " lost connection to game.");
				this.getChannel().post(new MumbleLinkAvatarChangeEvent(this.currentState.get().getAvatar().orNull(), null));
				this.getChannel().post(new MumbleLinkMapChangeEvent(this.currentState.get().getMapId().get(), null));

				this.getChannel().post(new MumbleLinkAvatarFrontChangeEvent(this.currentState.get().getAvatarFront().get(), null));
				this.getChannel().post(new MumbleLinkAvatarTopChangeEvent(this.currentState.get().getAvatarTop().get(), null));
				this.getChannel().post(new MumbleLinkAvatarPositionChangeEvent(this.currentState.get().getAvatarPosition().get(), null));
				this.getChannel().post(new MumbleLinkCameraFrontChangeEvent(this.currentState.get().getCameraFront().get(), null));
				this.getChannel().post(new MumbleLinkCameraTopChangeEvent(this.currentState.get().getCameraTop().get(), null));
				this.getChannel().post(new MumbleLinkCameraPositionChangeEvent(this.currentState.get().getCameraPosition().get(), null));
				this.lastState = this.currentState;
				this.currentState = Optional.absent();
			}
		} catch (Throwable t) {
			LOGGER.error("Failed to run iteration of {}", this, t);
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("lastState", this.lastState).add("currentState", this.currentState).toString();
	}

	@Override
	public EventBus getChannel() {
		return this.channel;
	}
}
