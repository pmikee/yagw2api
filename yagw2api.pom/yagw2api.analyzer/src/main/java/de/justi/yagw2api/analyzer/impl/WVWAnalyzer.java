package de.justi.yagw2api.analyzer.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Date;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.IWVWMatchEntityDAO;
import de.justi.yagw2api.analyzer.IWorldEnityDAO;
import de.justi.yagw2api.analyzer.IWorldEntity;
import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveUnclaimedEvent;
import de.justi.yagw2api.wrapper.IWorld;

final class WVWAnalyzer implements IWVWAnalyzer {
	private static final Logger LOGGER = Logger.getLogger(WVWAnalyzer.class);
	@Inject
	private IWorldEnityDAO worldEntityDAO;
	@Inject
	private IWVWMatchEntityDAO wvwMatchEntityDAO;

	@Override
	public void onMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(event);
		}
		this.synchronizeWorldsOfMatch(event.getTimestamp().getTime(), event.getMatch());
	}

	@Override
	public void onObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(event);
		}
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getTimestamp().getTime(), event.getMap().getMatch().get());
		}
	}

	@Override
	public void onObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(event);
		}
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getTimestamp().getTime(), event.getMap().getMatch().get());
		}
	}

	@Override
	public void onObjectiveClaimedEvent(IWVWObjectiveClaimedEvent event) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(event);
		}
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getTimestamp().getTime(), event.getMap().getMatch().get());
		}
	}

	@Override
	public void onObjectiveUnclaimedEvent(IWVWObjectiveUnclaimedEvent event) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(event);
		}
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getTimestamp().getTime(), event.getMap().getMatch().get());
		}
	}

	@Override
	public void onInitializedMatchForWrapper(IWVWInitializedMatchEvent event) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(event);
		}
		this.synchronizeWorldsOfMatch(event.getTimestamp().getTime(), event.getMatch());
	}

	@Override
	public void onChangedMapScoreEvent(IWVWMapScoresChangedEvent event) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(event);
		}
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getTimestamp().getTime(), event.getMap().getMatch().get());
		}
	}

	private boolean synchronizeWorldsOfMatch(Date timestamp, IWVWMatch match) {
		checkNotNull(match);
		final IWVWMatchEntity entity = this.wvwMatchEntityDAO.findOrCreateWVWMatchEntityOf(match);
		checkState(entity.getBlueMap() != null);
		checkState(entity.getRedMap() != null);
		checkState(entity.getGreenMap() != null);
		checkState(entity.getCenterMap() != null);
		checkState(entity.getBlueMap() != entity.getRedMap());
		checkState(entity.getBlueMap().getId() != entity.getRedMap().getId());
		checkState(entity.getBlueMap() != entity.getGreenMap());
		checkState(entity.getBlueMap().getId() != entity.getGreenMap().getId());
		checkState(entity.getBlueMap() != entity.getCenterMap());
		checkState(entity.getBlueMap().getId() != entity.getCenterMap().getId());

		checkState(entity.getBlueWorld() != null);
		checkState(entity.getRedWorld() != null);
		checkState(entity.getGreenWorld() != null);

		boolean persisted = false;
		this.wvwMatchEntityDAO.synchronizeEntityWithModel(entity, timestamp, match);
		checkState(entity.getBlueMap() != null);
		checkState(entity.getRedMap() != null);
		checkState(entity.getGreenMap() != null);
		checkState(entity.getCenterMap() != null);
		checkState(entity.getBlueMap() != entity.getRedMap());
		checkState(entity.getBlueMap().getId() != entity.getRedMap().getId());
		checkState(entity.getBlueMap() != entity.getGreenMap());
		checkState(entity.getBlueMap().getId() != entity.getGreenMap().getId());
		checkState(entity.getBlueMap() != entity.getCenterMap());
		checkState(entity.getBlueMap().getId() != entity.getCenterMap().getId());

		checkState(entity.getBlueWorld() != null);
		checkState(entity.getRedWorld() != null);
		checkState(entity.getGreenWorld() != null);
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Successfully synchronized " + IWVWMatchEntity.class.getSimpleName() + " with matchId=" + entity.getOriginMatchId());
		}
		persisted = this.wvwMatchEntityDAO.save(entity);

		if (LOGGER.isDebugEnabled()) {
			if (persisted) {
				if (LOGGER.isTraceEnabled()) {
					LOGGER.trace("Successfully persisted " + IWVWMatchEntity.class.getSimpleName() + " with matchId=" + entity.getOriginMatchId());
				}
			}
		}
		if (!persisted) {
			LOGGER.error("Failed to persist synchronized " + IWVWMatchEntity.class.getSimpleName() + " with matchId=" + entity.getOriginMatchId());
		}
		return persisted;
	}

	@Override
	public IWorldEntity worldEntityOf(IWorld world) {
		return this.worldEntityDAO.findOrCreateWorldEntityOf(world);
	}

}
