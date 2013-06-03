package de.justi.yagw2api.core.wrapper.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.yagw2api.core.AbstractEvent;
import de.justi.yagw2api.core.wrapper.events.IInitializedWVWMatchForWrapperEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;

public class InitializedWVWMatchForWrapperEvent extends AbstractEvent implements IInitializedWVWMatchForWrapperEvent{
	private final IWVWMatch match;
	
	public InitializedWVWMatchForWrapperEvent(IWVWMatch match) {
		super();
		this.match = checkNotNull(match);
	}
	
	@Override
	public IWVWMatch getInitializedMatch() {
		return this.match;
	}

}
