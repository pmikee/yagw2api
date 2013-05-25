package model.wvw;

import java.util.Calendar;

public interface IWVWObjectiveEvent {
	IWVWObjective getSource();
	Calendar getTimestamp();
}