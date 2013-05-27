package de.justi.gw2.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.gw2.model.AbstractEvent;
import de.justi.gw2.model.wvw.events.IWVWObjectiveEvent;
import de.justi.gw2.model.wvw.types.IWVWObjective;

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
