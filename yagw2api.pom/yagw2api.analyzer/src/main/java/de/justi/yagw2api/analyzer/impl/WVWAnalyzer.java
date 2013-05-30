package de.justi.yagw2api.analyzer.impl;

import org.apache.log4j.Logger;

import de.justi.yagw2api.analyzer.wvw.IWVWAnalyzer;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

class WVWAnalyzer implements IWVWAnalyzer {
	private static final Logger LOGGER = Logger.getLogger(WVWAnalyzer.class);
	@Override
	public void notifyAboutMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event) {
		LOGGER.debug(event);
	}

	@Override
	public void notifyAboutObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event) {
		LOGGER.debug(event);
	}

	@Override
	public void notifyAboutObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
		LOGGER.debug(event);
	}

	@Override
	public void notifyAboutChangedMapScoreEvent(IWVWMapScoresChangedEvent event) {
		LOGGER.debug(event);
	}

}
