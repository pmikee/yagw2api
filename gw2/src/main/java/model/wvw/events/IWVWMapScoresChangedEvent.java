package model.wvw.events;

import model.wvw.IWVWMap;

public interface IWVWMapScoresChangedEvent extends IWVWScoresChangedEvent {
	IWVWMap getMap();
}
