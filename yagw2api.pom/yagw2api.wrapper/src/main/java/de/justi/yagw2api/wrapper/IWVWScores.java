package de.justi.yagw2api.wrapper;

import de.justi.yagwapi.common.IHasChannel;

public interface IWVWScores extends IHasChannel {
	int getRedScore();
	int getGreenScore();
	int getBlueScore();
	
	void update(int redScore, int greenScore, int blueScore);
	IWVWScores createUnmodifiableReference();
}