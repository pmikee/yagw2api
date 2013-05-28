package de.justi.yagw2api.wrapper.model.wvw.events;

import de.justi.yagw2api.wrapper.model.wvw.IWVWScores;

public interface IWVWScoresChangedEvent {
	IWVWScores getScores();
	int getDeltaRed();
	int getDeltaGreen();
	int getDeltaBlue();
}
