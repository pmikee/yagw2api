package de.justi.yagw2api.mumblelink.impl;

import javax.annotation.Nullable;

import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarPositionChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;

final class MumbleLinkAvatarPositionChangeEvent extends AbstractMumbleLinkPositionChangeEvent implements IMumbleLinkAvatarPositionChangeEvent {
	public MumbleLinkAvatarPositionChangeEvent(@Nullable IMumbleLinkPosition oldPosition, @Nullable IMumbleLinkPosition newPosition) {
		super(oldPosition, newPosition);
	}
}
