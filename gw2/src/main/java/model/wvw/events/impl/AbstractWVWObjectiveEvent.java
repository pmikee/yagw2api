package model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.AbstractEvent;
import model.wvw.events.IWVWObjectiveEvent;
import model.wvw.types.IWVWObjective;

abstract class AbstractWVWObjectiveEvent extends AbstractEvent implements IWVWObjectiveEvent {
	private final IWVWObjective source;
	
	public AbstractWVWObjectiveEvent(IWVWObjective source) {
		super();
		this.source = checkNotNull(source);
	}
	
	public IWVWObjective getSource() {
		return this.source;
	}

}
