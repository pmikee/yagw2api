package de.justi.yagw2api.mumblelink;

import com.google.common.base.Optional;

public interface IMumbleLinkState {

	Optional<Integer> getUIVersion();

	Optional<Integer> getUITick();

	Optional<String> getGameName();

	Optional<String> getAvatarName();

	Optional<IMumbleLinkPosition> getAvatarPosition();

	Optional<IMumbleLinkPosition> getAvatarFront();

	Optional<IMumbleLinkPosition> getAvatarTop();

	Optional<IMumbleLinkPosition> getCameraPosition();

	Optional<IMumbleLinkPosition> getCameraFront();

	Optional<IMumbleLinkPosition> getCameraTop();

	Optional<Integer> getContextLength();

	Optional<byte[]> getContext();
}
