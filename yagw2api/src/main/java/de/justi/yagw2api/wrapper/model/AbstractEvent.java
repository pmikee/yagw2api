package de.justi.yagw2api.wrapper.model;

import java.text.DateFormat;
import java.util.Calendar;

import com.google.common.base.Objects;

public abstract class AbstractEvent implements IEvent {
	private final Calendar timestamp;

	public AbstractEvent() {
		this.timestamp = Calendar.getInstance();
	}

	public final Calendar getTimestamp() {
		return this.timestamp;
	}

	public String toString() {
		final DateFormat df = DateFormat.getDateTimeInstance();
		return Objects.toStringHelper(this).add("timestamp", df.format(this.timestamp.getTime())).toString();
	}
}
