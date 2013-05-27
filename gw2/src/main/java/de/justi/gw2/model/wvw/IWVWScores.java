package de.justi.gw2.model.wvw;

import de.justi.gw2.model.IHasChannel;

public interface IWVWScores extends IHasChannel {
	int getRedScore();
	int getGreenScore();
	int getBlueScore();
	
	void update(int redScore, int greenScore, int blueScore);
	IWVWScores createImmutableReference();
}