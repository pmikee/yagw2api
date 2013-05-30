package de.justi.yagw2api.core.wrapper;

import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

public interface IWVWMapListener {
	void informAboutChangedMapScore(IWVWMapScoresChangedEvent event);
	void informAboutObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event);
	void informAboutObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event);
}
