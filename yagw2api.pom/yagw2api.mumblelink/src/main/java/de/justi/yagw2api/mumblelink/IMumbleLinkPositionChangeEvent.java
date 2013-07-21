package de.justi.yagw2api.mumblelink;

import com.google.common.base.Optional;

public interface IMumbleLinkPositionChangeEvent extends IMumbleLinkEvent {
	Optional<IMumbleLinkPosition> getOldPosition();

	Optional<IMumbleLinkPosition> getNewPosition();
}
