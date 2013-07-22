package de.justi.yagw2api.mumblelink.impl;

import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarFrontChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarPositionChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarTopChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraFrontChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraPositionChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraTopChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkMapChangeEvent;

public interface IMumbleLinkListener {
	void onAvatarChange(IMumbleLinkAvatarChangeEvent event);

	void onMapChange(IMumbleLinkMapChangeEvent event);

	void onAvatarPositionChange(IMumbleLinkAvatarPositionChangeEvent event);

	void onAvatarFrontChange(IMumbleLinkAvatarFrontChangeEvent event);

	void onAvatarTopChange(IMumbleLinkAvatarTopChangeEvent event);

	void onCameraPositionChange(IMumbleLinkCameraPositionChangeEvent event);

	void onCameraFrontChange(IMumbleLinkCameraFrontChangeEvent event);

	void onCameraTopChange(IMumbleLinkCameraTopChangeEvent event);
}
