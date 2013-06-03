package de.justi.yagw2api.core.wrapper.events;

import de.justi.yagw2api.core.IEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;

public interface IInitializedWVWMatchForWrapperEvent extends IEvent {
	IWVWMatch getInitializedMatch();
}
