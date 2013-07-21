package de.justi.yagw2api.mumblelink.impl;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;
import de.justi.yagw2api.mumblelink.IMumbleLinkState;

abstract class MumbleLinkStateDelegate implements IMumbleLinkState {
	@Nullable
	protected abstract IMumbleLinkState getDelegate();

	@Override
	public final Optional<Integer> getUIVersion() {
		return this.getDelegate() != null ? this.getDelegate().getUIVersion() : Optional.<Integer> absent();
	}

	@Override
	public final Optional<Integer> getUITick() {
		return this.getDelegate() != null ? this.getDelegate().getUITick() : Optional.<Integer> absent();
	}

	@Override
	public final Optional<String> getGameName() {
		return this.getDelegate() != null ? this.getDelegate().getGameName() : Optional.<String> absent();
	}

	@Override
	public final Optional<String> getAvatarName() {
		return this.getDelegate() != null ? this.getDelegate().getAvatarName() : Optional.<String> absent();
	}

	@Override
	public final Optional<IMumbleLinkPosition> getAvatarPosition() {
		return this.getDelegate() != null ? this.getDelegate().getAvatarPosition() : Optional.<IMumbleLinkPosition> absent();
	}

	@Override
	public final Optional<IMumbleLinkPosition> getAvatarFront() {
		return this.getDelegate() != null ? this.getDelegate().getAvatarFront() : Optional.<IMumbleLinkPosition> absent();
	}

	@Override
	public final Optional<IMumbleLinkPosition> getAvatarTop() {
		return this.getDelegate() != null ? this.getDelegate().getAvatarTop() : Optional.<IMumbleLinkPosition> absent();
	}

	@Override
	public final Optional<IMumbleLinkPosition> getCameraPosition() {
		return this.getDelegate() != null ? this.getDelegate().getCameraPosition() : Optional.<IMumbleLinkPosition> absent();
	}

	@Override
	public final Optional<IMumbleLinkPosition> getCameraFront() {
		return this.getDelegate() != null ? this.getDelegate().getCameraFront() : Optional.<IMumbleLinkPosition> absent();
	}

	@Override
	public final Optional<IMumbleLinkPosition> getCameraTop() {
		return this.getDelegate() != null ? this.getDelegate().getCameraTop() : Optional.<IMumbleLinkPosition> absent();
	}

	@Override
	public final Optional<Integer> getContextLength() {
		return this.getDelegate() != null ? this.getDelegate().getContextLength() : Optional.<Integer> absent();
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(MumbleLinkStateDelegate.class).addValue(this.getDelegate()).toString();
	}

	@Override
	public Optional<Integer> getRegionId() {
		return this.getDelegate() != null ? this.getDelegate().getRegionId() : Optional.<Integer> absent();
	}

	@Override
	public Optional<Integer> getBuild() {
		return this.getDelegate() != null ? this.getDelegate().getBuild() : Optional.<Integer> absent();
	}

	@Override
	public Optional<Integer> getMapId() {
		return this.getDelegate() != null ? this.getDelegate().getMapId() : Optional.<Integer> absent();
	}
}
