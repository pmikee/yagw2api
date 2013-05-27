package de.justi.gw2.model.wvw.events.impl;

import de.justi.gw2.model.wvw.IWVWObjective;
import de.justi.gw2.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

public class WVWObjectiveEndOfBuffEvent extends AbstractWVWObjectiveEvent implements IWVWObjectiveEndOfBuffEvent {
	public WVWObjectiveEndOfBuffEvent(IWVWObjective source) {
		super(source);
	}
}
