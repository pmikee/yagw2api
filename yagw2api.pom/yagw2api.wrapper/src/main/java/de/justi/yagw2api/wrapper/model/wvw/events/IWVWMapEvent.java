package de.justi.yagw2api.wrapper.model.wvw.events;

import de.justi.yagw2api.wrapper.model.wvw.IWVWMap;
import de.justi.yagwapi.common.IEvent;

public interface IWVWMapEvent extends IEvent{
	IWVWMap getMap();
}
