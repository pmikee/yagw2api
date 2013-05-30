package de.justi.yagw2api.core.wrapper.model.wvw;

import de.justi.yagw2api.core.wrapper.model.IHasChannel;

public interface IWVWScores extends IHasChannel {
	int getRedScore();
	int getGreenScore();
	int getBlueScore();
	
	void update(int redScore, int greenScore, int blueScore);
	IWVWScores createImmutableReference();
}