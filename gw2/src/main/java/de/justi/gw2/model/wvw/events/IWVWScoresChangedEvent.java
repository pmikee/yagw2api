package de.justi.gw2.model.wvw.events;

import de.justi.gw2.model.wvw.IWVWScores;

public interface IWVWScoresChangedEvent {
	IWVWScores getScores();
	int getDeltaRed();
	int getDeltaGreen();
	int getDeltaBlue();
}
