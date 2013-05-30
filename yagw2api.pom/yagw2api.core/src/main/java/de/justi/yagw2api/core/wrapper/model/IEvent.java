package de.justi.yagw2api.core.wrapper.model;

import java.util.Calendar;

public interface IEvent extends IImmutable{
	Calendar getTimestamp();
}
