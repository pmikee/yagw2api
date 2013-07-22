package de.justi.yagw2api.mumblelink;

import de.justi.yagw2api.mumblelink.impl.IMumbleLinkListener;

public interface IMumbleLink extends IMumbleLinkState {

	void registerMumbleLinkListener(IMumbleLinkListener listener);

	void unregisterMumbleLinkListener(IMumbleLinkListener listener);

	void setActive(boolean active);

	boolean isActive();
}
