package de.justi.yagw2api.wrapper.impl;

import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWVWObjectiveEndOfBuffEvent;

final class WVWObjectiveEndOfBuffEvent extends AbstractWVWObjectiveEvent implements IWVWObjectiveEndOfBuffEvent {
	public WVWObjectiveEndOfBuffEvent(IWVWObjective source) {
		super(source);
	}
}
