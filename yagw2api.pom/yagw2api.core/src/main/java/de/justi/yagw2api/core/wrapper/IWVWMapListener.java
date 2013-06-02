package de.justi.yagw2api.core.wrapper;

import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

public interface IWVWMapListener {
	void notifyAboutChangedMapScoreEvent(IWVWMapScoresChangedEvent event);
	void notifyAboutObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event);
	void notifyAboutObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event);
	void notifyAboutObjectiveClaimedEvent(IWVWObjectiveClaimedEvent event);
}
