package de.justi.yagw2api.core.wrapper;

import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveUnclaimedEvent;

public interface IWVWMapListener {
	void onChangedMapScoreEvent(IWVWMapScoresChangedEvent event);

	void onObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event);

	void onObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event);

	void onObjectiveClaimedEvent(IWVWObjectiveClaimedEvent event);

	void onObjectiveUnclaimedEvent(IWVWObjectiveUnclaimedEvent event);
}
