package model;

import java.util.Calendar;

public interface IWVWObjectiveEvent {
	IWVWObjective getSource();
	Calendar getTimestamp();
}