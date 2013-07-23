package de.justi.yagw2api.wrapper;


public interface IWVWMapListener {
	void onChangedMapScoreEvent(IWVWMapScoresChangedEvent event);

	void onObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event);

	void onObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event);

	void onObjectiveClaimedEvent(IWVWObjectiveClaimedEvent event);

	void onObjectiveUnclaimedEvent(IWVWObjectiveUnclaimedEvent event);
}
