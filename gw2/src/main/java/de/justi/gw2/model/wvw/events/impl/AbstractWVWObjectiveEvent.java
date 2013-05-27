package de.justi.gw2.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.gw2.model.AbstractEvent;
import de.justi.gw2.model.wvw.IWVWObjective;
import de.justi.gw2.model.wvw.events.IWVWObjectiveEvent;

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
