package de.justi.yagw2api.mumblelink.impl;

import javax.annotation.Nullable;

import de.justi.yagw2api.mumblelink.IMumbleLinkCameraPositionChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;

final class MumbleLinkCameraPositionChangeEvent extends AbstractMumbleLinkPositionChangeEvent implements IMumbleLinkCameraPositionChangeEvent {
	public MumbleLinkCameraPositionChangeEvent(@Nullable IMumbleLinkPosition oldPosition, @Nullable IMumbleLinkPosition newPosition) {
		super(oldPosition, newPosition);
	}
}
