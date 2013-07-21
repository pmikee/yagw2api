package de.justi.yagw2api.mumblelink.impl;

import javax.annotation.Nullable;

import de.justi.yagw2api.mumblelink.IMumbleLinkCameraFrontChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;

final class MumbleLinkCameraFrontChangeEvent extends AbstractMumbleLinkPositionChangeEvent implements IMumbleLinkCameraFrontChangeEvent {
	public MumbleLinkCameraFrontChangeEvent(@Nullable IMumbleLinkPosition oldPosition, @Nullable IMumbleLinkPosition newPosition) {
		super(oldPosition, newPosition);
	}
}
