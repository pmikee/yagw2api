package de.justi.yagw2api.wrapper.model.wvw.events;

import de.justi.yagw2api.wrapper.model.wvw.IWVWMap;

public interface IWVWMapScoresChangedEvent extends IWVWScoresChangedEvent {
	IWVWMap getMap();	
}
