package de.justi.yagw2api.mumblelink;

import com.google.common.base.Optional;

public interface IMumbleLinkMapChangeEvent extends IMumbleLinkEvent {
	Optional<Integer> getOldMapId();

	Optional<Integer> getNewMapId();
}
