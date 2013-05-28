package de.justi.yagw2api.analyzer.wvw.impl;

import org.apache.log4j.Logger;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.model.wvw.IWVWObjective;

class WVWAnalyzer implements IWVWAnalyzer {
	private static final Logger LOGGER = Logger.getLogger(WVWAnalyzer.class);
	@Override
	public void notifyAboutUpdatedMapScores(IWVWMap map) {
		LOGGER.debug("Notified about map score change for " + map.getType().getLabel() + " of " + map.getMatch().get().getId() + " to " + map.getScores());
	}

	@Override
	public void notifyAboutUpdatedMatchScores(IWVWMatch match) {
		LOGGER.debug("Notified about match score change for " + match.getId() + " to " + match.getScores());
	}

	@Override
	public void notifyAboutCapturedObjective(IWVWObjective objective) {
		LOGGER.debug("Notified about captured " + objective.getLabel() + " on " + objective.getMap().get().getType() + "-map in match("
				+ objective.getMap().get().getMatch().get().getId() + ") by " + objective.getOwner().get().getName());
	}

	@Override
	public void notifyAboutEndOfBuffObjective(IWVWObjective objective) {
		LOGGER.debug("Notified about end of buff for " + objective.getLabel() + " on " + objective.getMap().get().getType() + "-map in match("
				+ objective.getMap().get().getMatch().get().getId() + ") by " + objective.getOwner().get().getName());
	}

}
