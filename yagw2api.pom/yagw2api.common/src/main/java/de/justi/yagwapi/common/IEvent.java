package de.justi.yagwapi.common;

import java.util.Calendar;

public interface IEvent extends IImmutable {
	Calendar getTimestamp();
}
