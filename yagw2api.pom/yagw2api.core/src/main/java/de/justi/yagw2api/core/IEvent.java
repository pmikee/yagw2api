package de.justi.yagw2api.core;

import java.util.Calendar;

import de.justi.yagw2api.core.wrapper.model.IImmutable;

public interface IEvent extends IImmutable{
	Calendar getTimestamp();
}
