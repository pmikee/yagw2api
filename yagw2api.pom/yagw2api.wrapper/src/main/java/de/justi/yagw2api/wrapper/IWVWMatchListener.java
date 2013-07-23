package de.justi.yagw2api.wrapper;


public interface IWVWMatchListener {
	void onMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event);
	void onInitializedMatchForWrapper(IWVWInitializedMatchEvent event);
}
