package de.justi.yagw2api.core.wrapper;

import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;

public interface IWVWMatchListener {
	void notifyAboutMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event);
}
