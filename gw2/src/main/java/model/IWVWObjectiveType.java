package model;

import java.util.concurrent.TimeUnit;

public interface IWVWObjectiveType {
	String getLabel();
	long getBuffDuration(TimeUnit timeUnit);
	int getPoints();
	
	boolean isCamp();
	boolean isTower();
	boolean isKeep();
	boolean isCastle();
}
