package de.justi.yagw2api.wrapper.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWObjectiveEvent;
import de.justi.yagwapi.common.AbstractEvent;

abstract class AbstractWVWObjectiveEvent extends AbstractEvent implements IWVWObjectiveEvent {
	private final IWVWObjective source;
	
	public AbstractWVWObjectiveEvent(IWVWObjective source) {
		super();
		this.source = checkNotNull(source);
	}
	
	public IWVWObjective getObjective() {
		return this.source;
	}
	
	@Override
	public IWVWMap getMap() {
		checkState(this.getObjective().getMap().isPresent());
		return this.getObjective().getMap().get();
	}
}
