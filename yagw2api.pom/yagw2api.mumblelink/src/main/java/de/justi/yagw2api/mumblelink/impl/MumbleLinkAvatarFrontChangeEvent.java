package de.justi.yagw2api.mumblelink.impl;

import javax.annotation.Nullable;

import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarFrontChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;

final class MumbleLinkAvatarFrontChangeEvent extends AbstractMumbleLinkPositionChangeEvent implements IMumbleLinkAvatarFrontChangeEvent {
	public MumbleLinkAvatarFrontChangeEvent(@Nullable IMumbleLinkPosition oldPosition, @Nullable IMumbleLinkPosition newPosition) {
		super(oldPosition, newPosition);
	}
}
