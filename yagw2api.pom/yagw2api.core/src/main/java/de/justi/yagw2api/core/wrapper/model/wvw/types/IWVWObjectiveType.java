package de.justi.yagw2api.core.wrapper.model.wvw.types;

import java.util.concurrent.TimeUnit;

import de.justi.yagw2api.core.wrapper.model.IImmutable;

public interface IWVWObjectiveType extends IImmutable{
	String getLabel();
	long getBuffDuration(TimeUnit timeUnit);
	int getPoints();
	
	boolean isCamp();
	boolean isTower();
	boolean isKeep();
	boolean isCastle();
}
