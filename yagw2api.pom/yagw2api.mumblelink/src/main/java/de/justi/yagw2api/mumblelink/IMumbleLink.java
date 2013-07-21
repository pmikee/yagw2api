package de.justi.yagw2api.mumblelink;

import de.justi.yagwapi.common.IHasChannel;

public interface IMumbleLink extends IMumbleLinkState, IHasChannel {

	void setActive(boolean active);

	boolean isActive();
}
