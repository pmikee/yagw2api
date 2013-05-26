package model.wvw.events;

import model.wvw.IWVWMatch;

public interface IWVWMatchScoresChangedEvent extends IWVWScoresChangedEvent {
	IWVWMatch getMatch();
}
