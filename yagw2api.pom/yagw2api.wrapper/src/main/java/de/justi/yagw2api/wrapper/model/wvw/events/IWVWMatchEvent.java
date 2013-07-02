package de.justi.yagw2api.wrapper.model.wvw.events;

import de.justi.yagw2api.wrapper.model.wvw.IWVWMatch;
import de.justi.yagwapi.common.IEvent;

public interface IWVWMatchEvent extends IEvent{
	IWVWMatch getMatch();
}
