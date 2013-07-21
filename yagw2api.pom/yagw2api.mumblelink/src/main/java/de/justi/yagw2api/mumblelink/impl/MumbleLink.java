package de.justi.yagw2api.mumblelink.impl;

import com.google.common.base.Objects;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.justi.yagw2api.mumblelink.IMumbleLink;
import de.justi.yagw2api.mumblelink.IMumbleLinkEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkState;

final class MumbleLink extends MumbleLinkStateDelegate implements IMumbleLink {

	private final MumbleLinkSynchronizerService mumbleLinkUpdater;
	private EventBus channel = new EventBus(this.getClass().getName());

	@Override
	public EventBus getChannel() {
		return this.channel;
	}

	@Override
	public boolean isActive() {
		return this.mumbleLinkUpdater.isRunning();
	}

	@Override
	protected IMumbleLinkState getDelegate() {
		return this.mumbleLinkUpdater.getCurrentSate().orNull();
	}

	MumbleLink() {
		this.mumbleLinkUpdater = new MumbleLinkSynchronizerService();
		this.mumbleLinkUpdater.getChannel().register(this);
		this.mumbleLinkUpdater.start();
	}

	@Subscribe
	public final void forwardEvent(IMumbleLinkEvent event) {
		this.getChannel().post(event);
	}

	@Override
	public void setActive(boolean active) {
		if (active != this.isActive()) {
			if (active) {
				this.mumbleLinkUpdater.startAndWait();
			} else {
				this.mumbleLinkUpdater.stopAndWait();
			}
		}
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("active", this.isActive()).addValue(super.toString()).toString();
	}

}
