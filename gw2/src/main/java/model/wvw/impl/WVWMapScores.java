package model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.wvw.IWVWMap;
import model.wvw.events.IWVWModelEventFactory;
import utils.InjectionHelper;

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
