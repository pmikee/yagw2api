package de.justi.gw2.model.wvw.events;

import de.justi.gw2.model.wvw.IWVWMap;

public interface IWVWMapScoresChangedEvent extends IWVWScoresChangedEvent {
	IWVWMap getMap();
}
