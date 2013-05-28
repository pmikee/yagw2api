package de.justi.yagw2api.wrapper.model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import org.apache.log4j.Logger;

import de.justi.yagw2api.utils.YAGW2APIInjectionHelper;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWModelEventFactory;

class WVWMapScores extends AbstractWVWScores {
	private static final Logger LOGGER = Logger.getLogger(WVWMapScores.class);
	private static final IWVWModelEventFactory WVW_MODEL_EVENT_FACTORY = YAGW2APIInjectionHelper.getInjector().getInstance(IWVWModelEventFactory.class);
	
	private final IWVWMap map;
	
	public WVWMapScores(IWVWMap map) {
		this.map = checkNotNull(map);
	}
	
	@Override
	protected void onChange(int deltaRed, int deltaGreen, int deltaBlue) {
		final IWVWMapScoresChangedEvent event = WVW_MODEL_EVENT_FACTORY.newMapScoresChangedEvent(this.createImmutableReference(),deltaRed, deltaGreen, deltaBlue, this.map.createImmutableReference());
		checkState(event != null);
		if(LOGGER.isTraceEnabled()) {
			LOGGER.trace("Going to post new "+event);
		}
		this.getChannel().post(event);
	}

}
