package de.justi.gw2.model.wvw.events;

import de.justi.gw2.model.wvw.IWVWMatch;

public interface IWVWMatchScoresChangedEvent extends IWVWScoresChangedEvent {
	IWVWMatch getMatch();
}
