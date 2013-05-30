package de.justi.yagw2api.core.wrapper.model.wvw.events;

import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;

public interface IWVWMapScoresChangedEvent extends IWVWScoresChangedEvent {
	IWVWMap getMap();	
}
