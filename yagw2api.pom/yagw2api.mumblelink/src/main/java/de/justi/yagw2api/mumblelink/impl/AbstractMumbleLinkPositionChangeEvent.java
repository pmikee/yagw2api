package de.justi.yagw2api.mumblelink.impl;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;
import de.justi.yagw2api.mumblelink.IMumbleLinkPositionChangeEvent;
import de.justi.yagwapi.common.AbstractEvent;

abstract class AbstractMumbleLinkPositionChangeEvent extends AbstractEvent implements IMumbleLinkPositionChangeEvent {

	private final Optional<IMumbleLinkPosition> oldPosition;
	private final Optional<IMumbleLinkPosition> newPosition;

	protected AbstractMumbleLinkPositionChangeEvent(@Nullable IMumbleLinkPosition oldPosition, @Nullable IMumbleLinkPosition newPosition) {
		checkArgument(((oldPosition == null) ^ (newPosition == null)) || ((oldPosition != null) && !oldPosition.equals(newPosition)) || ((newPosition != null) && !newPosition.equals(oldPosition)),
				"Old position = %s and new position = %s have to be different.", oldPosition, newPosition);
		this.oldPosition = Optional.fromNullable(oldPosition);
		this.newPosition = Optional.fromNullable(newPosition);
	}

	@Override
	public Optional<IMumbleLinkPosition> getOldPosition() {
		return oldPosition;
	}

	@Override
	public Optional<IMumbleLinkPosition> getNewPosition() {
		return newPosition;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).addValue(super.toString()).add("old", this.oldPosition).add("new", this.newPosition).toString();
	}
}
