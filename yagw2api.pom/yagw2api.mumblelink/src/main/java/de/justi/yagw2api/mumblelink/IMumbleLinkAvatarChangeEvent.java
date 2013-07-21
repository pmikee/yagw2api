package de.justi.yagw2api.mumblelink;

import com.google.common.base.Optional;

public interface IMumbleLinkAvatarChangeEvent extends IMumbleLinkEvent {
	Optional<String> getOldAvatarName();

	Optional<String> getNewAvatarName();
}
