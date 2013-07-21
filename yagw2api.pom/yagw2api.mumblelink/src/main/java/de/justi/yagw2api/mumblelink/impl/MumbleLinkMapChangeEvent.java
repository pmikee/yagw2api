package de.justi.yagw2api.mumblelink.impl;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import de.justi.yagw2api.mumblelink.IMumbleLinkMapChangeEvent;
import de.justi.yagwapi.common.AbstractEvent;

final class MumbleLinkMapChangeEvent extends AbstractEvent implements IMumbleLinkMapChangeEvent {
	private final Optional<Integer> oldMapId;
	private final Optional<Integer> newMapId;

	public MumbleLinkMapChangeEvent(@Nullable Integer oldMapId, @Nullable Integer newMapId) {
		this.oldMapId = Optional.fromNullable(oldMapId);
		this.newMapId = Optional.fromNullable(newMapId);
	}

	@Override
	public Optional<Integer> getOldMapId() {
		return this.oldMapId;
	}

	@Override
	public Optional<Integer> getNewMapId() {
		return this.newMapId;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).addValue(super.toString()).add("oldMapId", this.oldMapId).add("newMapId", this.newMapId).toString();
	}
}
