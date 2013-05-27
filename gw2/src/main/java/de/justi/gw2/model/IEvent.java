package de.justi.gw2.model;

import java.util.Calendar;

public interface IEvent extends IImmutable{
	Calendar getTimestamp();
}
