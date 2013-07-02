package de.justi.yagw2api.wrapper.model.wvw.events;

import de.justi.yagw2api.wrapper.model.wvw.IWVWObjective;

public interface IWVWObjectiveEvent extends IWVWMapEvent{
	IWVWObjective getObjective();
}