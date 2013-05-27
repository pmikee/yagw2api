package de.justi.gw2.model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.gw2.model.wvw.IWVWMap;
import de.justi.gw2.model.wvw.events.IWVWModelEventFactory;
import de.justi.gw2.utils.InjectionHelper;

public class WVWMapScores extends AbstractWVWScores {
	private static final IWVWModelEventFactory WVW_MODEL_EVENT_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelEventFactory.class);
	
	private final IWVWMap map;
	
	public WVWMapScores(IWVWMap map) {
		this.map = checkNotNull(map);
	}
	
	@Override
	protected void onChange() {
		this.getChannel().post(WVW_MODEL_EVENT_FACTORY.newMapScoresChangedEvent(this.createImmutableReference(), this.map.createImmutableReference()));
	}

}
