package de.justi.yagw2api.core.wrapper.model.wvw.events;

import de.justi.yagw2api.core.wrapper.model.wvw.IWVWScores;

public interface IWVWScoresChangedEvent {
	IWVWScores getScores();
	int getDeltaRed();
	int getDeltaGreen();
	int getDeltaBlue();
}
