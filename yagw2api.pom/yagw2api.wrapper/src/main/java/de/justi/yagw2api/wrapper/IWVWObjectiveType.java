package de.justi.yagw2api.wrapper;

import java.util.concurrent.TimeUnit;

import de.justi.yagwapi.common.IImmutable;

public interface IWVWObjectiveType extends IImmutable{
	String getLabel();
	long getBuffDuration(TimeUnit timeUnit);
	int getPoints();
	
	boolean isCamp();
	boolean isTower();
	boolean isKeep();
	boolean isCastle();
}
