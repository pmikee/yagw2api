package de.justi.yagw2api.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
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
