package de.justi.yagw2api.core.wrapper.model.wvw.events.impl;

import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

class WVWObjectiveEndOfBuffEvent extends AbstractWVWObjectiveEvent implements IWVWObjectiveEndOfBuffEvent {
	public WVWObjectiveEndOfBuffEvent(IWVWObjective source) {
		super(source);
	}
}
