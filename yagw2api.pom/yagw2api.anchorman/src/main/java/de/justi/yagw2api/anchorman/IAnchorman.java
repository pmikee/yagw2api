package de.justi.yagw2api.anchorman;

import de.justi.yagw2api.mumblelink.IMumbleLink;
import de.justi.yagw2api.wrapper.IWVWWrapper;

public interface IAnchorman {
	void init(IWVWWrapper wrapper, IMumbleLink mumblelink);

	void start();

	void stop();

	boolean isRunning();
}
