package de.justi.yagw2api.mumblelink.impl;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.base.Objects;
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
import de.justi.yagwapi.common.IHasChannel;

final class MumbleLinkSynchronizerService extends AbstractScheduledService implements IHasChannel {
	private static final int INITIAL_UI_TICK_VALUE = -1;
	private static final int DELAY_MILLIS = 500;
	private static final Logger LOGGER = Logger.getLogger(MumbleLinkSynchronizerService.class);
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
						this.lastUITick = this.currentState.isPresent() ? this.currentState.get().getUITick().get() : lastUITick;
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
				LOGGER.trace(this + " synchronized state with game.");
				if (!this.currentState.get().getAvatarName().equals(this.lastState.get().getAvatarName())) {
					this.getChannel().post(new MumbleLinkAvatarChangeEvent(this.lastState.get().getAvatarName().orNull(), this.currentState.get().getAvatarName().orNull()));
				}
				if (!this.currentState.get().getAvatarFront().equals(this.lastState.get().getAvatarFront())) {
					this.getChannel().post(new MumbleLinkAvatarFrontChangeEvent(this.lastState.get().getAvatarFront().orNull(), this.currentState.get().getAvatarFront().orNull()));
				}
				if (!this.currentState.get().getAvatarTop().equals(this.lastState.get().getAvatarTop())) {
					this.getChannel().post(new MumbleLinkAvatarTopChangeEvent(this.lastState.get().getAvatarTop().orNull(), this.currentState.get().getAvatarTop().orNull()));
				}
				if (!this.currentState.get().getAvatarPosition().equals(this.lastState.get().getAvatarPosition())) {
					this.getChannel().post(new MumbleLinkAvatarPositionChangeEvent(this.lastState.get().getAvatarPosition().orNull(), this.currentState.get().getAvatarPosition().orNull()));
				}
				if (!this.currentState.get().getCameraFront().equals(this.lastState.get().getCameraFront())) {
					this.getChannel().post(new MumbleLinkCameraFrontChangeEvent(this.lastState.get().getCameraFront().orNull(), this.currentState.get().getCameraFront().orNull()));
				}
				if (!this.currentState.get().getCameraTop().equals(this.lastState.get().getCameraTop())) {
					this.getChannel().post(new MumbleLinkCameraTopChangeEvent(this.lastState.get().getCameraTop().orNull(), this.currentState.get().getCameraTop().orNull()));
				}
				if (!this.currentState.get().getCameraPosition().equals(this.lastState.get().getCameraPosition())) {
					this.getChannel().post(new MumbleLinkCameraPositionChangeEvent(this.lastState.get().getCameraPosition().orNull(), this.currentState.get().getCameraPosition().orNull()));
				}
				if (!this.currentState.get().getMapId().equals(this.lastState.get().getMapId())) {
					this.getChannel().post(new MumbleLinkMapChangeEvent(this.lastState.get().getMapId().orNull(), this.currentState.get().getMapId().orNull()));
				}
			} else if (this.currentState.isPresent()) {
				// non dirty state but lost connectivity
				LOGGER.warn(this + " lost connection to game.");
				this.getChannel().post(new MumbleLinkAvatarChangeEvent(this.currentState.get().getAvatarName().orNull(), null));

				this.getChannel().post(new MumbleLinkAvatarFrontChangeEvent(this.currentState.get().getAvatarFront().orNull(), null));
				this.getChannel().post(new MumbleLinkAvatarTopChangeEvent(this.currentState.get().getAvatarTop().orNull(), null));
				this.getChannel().post(new MumbleLinkAvatarPositionChangeEvent(this.currentState.get().getAvatarPosition().orNull(), null));
				this.getChannel().post(new MumbleLinkCameraFrontChangeEvent(this.currentState.get().getCameraFront().orNull(), null));
				this.getChannel().post(new MumbleLinkCameraTopChangeEvent(this.currentState.get().getCameraTop().orNull(), null));
				this.getChannel().post(new MumbleLinkCameraPositionChangeEvent(this.currentState.get().getCameraPosition().orNull(), null));
				this.getChannel().post(new MumbleLinkMapChangeEvent(this.currentState.get().getMapId().orNull(), null));
				this.lastState = this.currentState;
				this.currentState = Optional.absent();
			}

		} catch (Exception e) {
			LOGGER.error("Failed to run iteration of " + this.getClass().getSimpleName(), e);
		} catch (Throwable t) {
			LOGGER.fatal("Failed to run iteration of " + this.getClass().getSimpleName(), t);
		}
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("lastState", this.lastState).add("currentState", this.currentState).toString();
	}

	@Override
	public EventBus getChannel() {
		return this.channel;
	}
}
