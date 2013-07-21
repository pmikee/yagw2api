package de.justi.yagw2api.mumblelink.impl;

import javax.annotation.Nullable;

import de.justi.yagw2api.mumblelink.IMumbleLinkCameraTopChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;

final class MumbleLinkCameraTopChangeEvent extends AbstractMumbleLinkPositionChangeEvent implements IMumbleLinkCameraTopChangeEvent {
	public MumbleLinkCameraTopChangeEvent(@Nullable IMumbleLinkPosition oldPosition, @Nullable IMumbleLinkPosition newPosition) {
		super(oldPosition, newPosition);
	}
}
