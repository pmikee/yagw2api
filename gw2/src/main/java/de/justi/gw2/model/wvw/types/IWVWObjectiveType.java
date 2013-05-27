package de.justi.gw2.model.wvw.types;

import java.util.concurrent.TimeUnit;

import de.justi.gw2.model.IImmutable;

public interface IWVWObjectiveType extends IImmutable{
	String getLabel();
	long getBuffDuration(TimeUnit timeUnit);
	int getPoints();
	
	boolean isCamp();
	boolean isTower();
	boolean isKeep();
	boolean isCastle();
}
