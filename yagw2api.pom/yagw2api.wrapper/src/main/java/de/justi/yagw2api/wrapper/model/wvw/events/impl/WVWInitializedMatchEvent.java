package de.justi.yagw2api.wrapper.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWInitializedMatchEvent;
import de.justi.yagwapi.common.AbstractEvent;

final class WVWInitializedMatchEvent extends AbstractEvent implements IWVWInitializedMatchEvent{
	private final IWVWMatch match;
	
	public WVWInitializedMatchEvent(IWVWMatch match) {
		super();
		this.match = checkNotNull(match);
	}
	
	@Override
	public IWVWMatch getMatch() {
		return this.match;
	}

}
