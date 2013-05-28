package de.justi.yagw2api.wrapper.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.yagw2api.wrapper.model.AbstractEvent;
import de.justi.yagw2api.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWObjectiveEvent;

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
