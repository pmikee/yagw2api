package de.justi.gw2.model.wvw.events;

import de.justi.gw2.model.IEvent;
import de.justi.gw2.model.wvw.types.IWVWObjective;

public interface IWVWObjectiveEvent extends IEvent{
	IWVWObjective getSource();
}