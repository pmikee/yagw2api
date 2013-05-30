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
	public void informAboutMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event) {
		LOGGER.debug(event);
	}

	@Override
	public void informAboutObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event) {
		LOGGER.debug(event);
	}

	@Override
	public void informAboutObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
		LOGGER.debug(event);
	}

	@Override
	public void informAboutChangedMapScore(IWVWMapScoresChangedEvent event) {
		LOGGER.debug(event);
	}

}
