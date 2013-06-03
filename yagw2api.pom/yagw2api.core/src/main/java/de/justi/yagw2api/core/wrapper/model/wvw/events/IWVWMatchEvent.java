package de.justi.yagw2api.core.wrapper.model.wvw.events;

import de.justi.yagw2api.core.IEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;

public interface IWVWMatchEvent extends IEvent{
	IWVWMatch getMatch();
}
