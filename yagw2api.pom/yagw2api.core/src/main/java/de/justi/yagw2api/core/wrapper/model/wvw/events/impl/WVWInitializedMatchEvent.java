package de.justi.yagw2api.core.wrapper.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.yagw2api.core.AbstractEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWInitializedMatchEvent;

class WVWInitializedMatchEvent extends AbstractEvent implements IWVWInitializedMatchEvent{
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
