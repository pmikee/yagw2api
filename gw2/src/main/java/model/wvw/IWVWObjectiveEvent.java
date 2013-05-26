package model.wvw;

import java.util.Calendar;

import model.wvw.types.IWVWObjective;

public interface IWVWObjectiveEvent {
	IWVWObjective getSource();
	Calendar getTimestamp();
}