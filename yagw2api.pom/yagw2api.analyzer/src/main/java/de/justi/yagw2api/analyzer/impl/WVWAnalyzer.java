package de.justi.yagw2api.analyzer.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.entities.IWorldEnityDAO;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntityDAO;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWInitializedMatchEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

class WVWAnalyzer implements IWVWAnalyzer {
	private static final Logger LOGGER = Logger.getLogger(WVWAnalyzer.class);
	@Inject
	private IWorldEnityDAO worldEntityDAO;
	@Inject
	private IWVWMatchEntityDAO wvwMatchEntityDAO;

	@Override
	public void onMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event) {
		LOGGER.debug(event);
		this.synchronizeWorldsOfMatch(event.getMatch());
	}

	@Override
	public void onObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event) {
		LOGGER.debug(event);
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getMap().getMatch().get());
		}
	}

	@Override
	public void onObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
		LOGGER.debug(event);
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getMap().getMatch().get());
		}
	}


	@Override
	public void onObjectiveClaimedEvent(IWVWObjectiveClaimedEvent event) {
		LOGGER.debug(event);
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getMap().getMatch().get());
		}
	}

	@Override
	public void onInitializedMatchForWrapper(IWVWInitializedMatchEvent event) {
		LOGGER.debug(event);
		this.synchronizeWorldsOfMatch(event.getMatch());		
	}

	@Override
	public void onChangedMapScoreEvent(IWVWMapScoresChangedEvent event) {
		LOGGER.debug(event);
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getMap().getMatch().get());
		}
	}

	private boolean synchronizeWorldsOfMatch(IWVWMatch match) {
		checkNotNull(match);
		final IWVWMatchEntity entity = this.wvwMatchEntityDAO.findOrCreateWVWMatchEntityOf(match);

		boolean persisted = false;
		if (entity.synchronizeWithModel(match, true)) {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Successfully synchronized "+IWVWMatchEntity.class.getSimpleName()+" with matchId="+entity.getOriginMatchId());
			}
			persisted = this.wvwMatchEntityDAO.save(entity);
			if (LOGGER.isDebugEnabled()) {
				if (persisted) {
					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace("Successfully persisted "+IWVWMatchEntity.class.getSimpleName()+" with matchId="+entity.getOriginMatchId());
					}
				} else {
					LOGGER.debug("Failed to persist synchronized "+IWVWMatchEntity.class.getSimpleName()+" with matchId="+entity.getOriginMatchId());
				}
			}
		} else if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Failed to persist synchronize "+IWVWMatchEntity.class.getSimpleName()+" with matchId="+entity.getOriginMatchId());
		}
		return persisted;
	}

	@Override
	public IWorldEntity worldEntityOf(IWorld world) {
		return this.worldEntityDAO.findOrCreateWorldEntityOf(world);
	}

}
