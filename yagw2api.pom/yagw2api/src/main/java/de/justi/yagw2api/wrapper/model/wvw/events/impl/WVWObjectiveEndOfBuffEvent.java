package de.justi.yagw2api.wrapper.model.wvw.events.impl;

import de.justi.yagw2api.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

class WVWObjectiveEndOfBuffEvent extends AbstractWVWObjectiveEvent implements IWVWObjectiveEndOfBuffEvent {
	public WVWObjectiveEndOfBuffEvent(IWVWObjective source) {
		super(source);
	}
}
