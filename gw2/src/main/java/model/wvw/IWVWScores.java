package model.wvw;

import model.IHasChannel;

public interface IWVWScores extends IHasChannel {
	int getRedScore();
	int getGreenScore();
	int getBlueScore();
	
	void update(int redScore, int greenScore, int blueScore);
	IWVWScores createImmutableReference();
}