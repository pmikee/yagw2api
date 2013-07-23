package de.justi.yagw2api.wrapper;


public interface IWVWScoresChangedEvent {
	IWVWScores getScores();
	int getDeltaRed();
	int getDeltaGreen();
	int getDeltaBlue();
}
