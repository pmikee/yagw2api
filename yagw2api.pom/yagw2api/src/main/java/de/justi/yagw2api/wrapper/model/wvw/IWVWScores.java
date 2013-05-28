package de.justi.yagw2api.wrapper.model.wvw;

import de.justi.yagw2api.wrapper.model.IHasChannel;

public interface IWVWScores extends IHasChannel {
	int getRedScore();
	int getGreenScore();
	int getBlueScore();
	
	void update(int redScore, int greenScore, int blueScore);
	IWVWScores createImmutableReference();
}