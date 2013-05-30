package de.justi.yagw2api.core.wrapper.model.wvw.events;

import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;

public interface IWVWMatchScoresChangedEvent extends IWVWScoresChangedEvent {
	IWVWMatch getMatch();
}
