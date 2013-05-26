package model.wvw.events;

import model.IEvent;
import model.wvw.types.IWVWObjective;

public interface IWVWObjectiveEvent extends IEvent{
	IWVWObjective getSource();
}