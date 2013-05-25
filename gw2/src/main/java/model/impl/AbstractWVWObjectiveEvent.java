package model.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Calendar;

import model.IWVWObjective;
import model.IWVWObjectiveEvent;

abstract class AbstractWVWObjectiveEvent implements IWVWObjectiveEvent {
	private final Calendar timestamp;
	private final IWVWObjective source;
	
	public AbstractWVWObjectiveEvent(IWVWObjective source) {
		this.source = checkNotNull(source);
		this.timestamp = Calendar.getInstance();
	}
	
	public IWVWObjective getSource() {
		return this.source;
	}

	public Calendar getTimestamp() {
		return this.timestamp;
	}

}
