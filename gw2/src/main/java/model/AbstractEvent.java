package model;

import java.util.Calendar;

public abstract class AbstractEvent implements IEvent {
	private final Calendar timestamp;

	public AbstractEvent () {
		this.timestamp = Calendar.getInstance();
	}

	public final Calendar getTimestamp() {
		return this.timestamp;
	}
}
