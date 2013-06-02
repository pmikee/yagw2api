package de.justi.yagw2api.analyzer.impl;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.entities.IWorldEnityDAO;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

class WVWAnalyzer implements IWVWAnalyzer {
	private static final Logger LOGGER = Logger.getLogger(WVWAnalyzer.class);
	@Inject
	private IWorldEnityDAO worldEntityDAO;
	
	@Override
	public void notifyAboutMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event) {
		LOGGER.debug(event);
		this.worldEntityOf(event.getMatch().getBlueWorld());
		this.worldEntityOf(event.getMatch().getRedWorld());
		this.worldEntityOf(event.getMatch().getGreenWorld());
	}

	@Override
	public void notifyAboutObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event) {
		LOGGER.debug(event);
		this.worldEntityOf(event.getMap().getMatch().get().getBlueWorld());
		this.worldEntityOf(event.getMap().getMatch().get().getRedWorld());
		this.worldEntityOf(event.getMap().getMatch().get().getGreenWorld());
	}

	@Override
	public void notifyAboutObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
		LOGGER.debug(event);
		this.worldEntityOf(event.getMap().getMatch().get().getBlueWorld());
		this.worldEntityOf(event.getMap().getMatch().get().getRedWorld());
		this.worldEntityOf(event.getMap().getMatch().get().getGreenWorld());
	}

	@Override
	public void notifyAboutChangedMapScoreEvent(IWVWMapScoresChangedEvent event) {
		LOGGER.debug(event);
		this.worldEntityOf(event.getMap().getMatch().get().getBlueWorld());
		this.worldEntityOf(event.getMap().getMatch().get().getRedWorld());
		this.worldEntityOf(event.getMap().getMatch().get().getGreenWorld());
	}

	@Override
	public IWorldEntity worldEntityOf(IWorld world) {
		return this.worldEntityDAO.findOrCreateWorldEntityOf(world);
	}

	@Override
	public void notifyAboutObjectiveClaimedEvent(IWVWObjectiveClaimedEvent event) {
	}

}
