package de.justi.yagw2api.wrapper;

import de.justi.yagw2api.wrapper.model.wvw.events.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;

public interface IWVWMatchListener {
	void onMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event);
	void onInitializedMatchForWrapper(IWVWInitializedMatchEvent event);
}
