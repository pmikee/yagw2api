package de.justi.yagw2api.analyzer.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.entities.IWorldEnityDAO;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;
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
		final IWorldEntity blueWorldEntity = this.worldEntityOf(match.getBlueWorld());
		final IWorldEntity redWorldEntity = this.worldEntityOf(match.getRedWorld());
		final IWorldEntity greenWorldEntity = this.worldEntityOf(match.getGreenWorld());

		boolean persisted = false;
		final boolean synchronizationCompleted = blueWorldEntity.synchronizeWithModel(match.getBlueWorld()) & redWorldEntity.synchronizeWithModel(match.getRedWorld())
				& greenWorldEntity.synchronizeWithModel(match.getGreenWorld());
		if (synchronizationCompleted) {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Successfully synchronized " + IWorldEntity.class.getSimpleName() + "s [blue=" + blueWorldEntity + ", green=" + greenWorldEntity + ", red=" + redWorldEntity
						+ "] with their " + IWorld.class.getSimpleName() + "s [blue=" + match.getBlueWorld() + ", green=" + match.getGreenWorld() + ", red=" + match.getRedWorld() + "]");
			}
			persisted = this.worldEntityDAO.save(blueWorldEntity) & this.worldEntityDAO.save(redWorldEntity) & this.worldEntityDAO.save(greenWorldEntity);
			if (LOGGER.isDebugEnabled()) {
				if (persisted) {
					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace("Successfully persisted synchronized " + IWorldEntity.class.getSimpleName() + "s: blue=" + blueWorldEntity + ", green=" + greenWorldEntity + ", red="
								+ redWorldEntity);
					}
				} else {
					LOGGER.debug("Failed to persist synchronized " + IWorldEntity.class.getSimpleName() + "s: blue=" + blueWorldEntity + ", green=" + greenWorldEntity + ", red=" + redWorldEntity);
				}
			}
		} else if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Failed to synchronize " + IWorldEntity.class.getSimpleName() + "s [blue=" + blueWorldEntity + ", green=" + greenWorldEntity + ", red=" + redWorldEntity + "] with their "
					+ IWorld.class.getSimpleName() + "s [blue=" + match.getBlueWorld() + ", green=" + match.getGreenWorld() + ", red=" + match.getRedWorld() + "]");
		}
		return synchronizationCompleted && persisted;
	}

	@Override
	public IWorldEntity worldEntityOf(IWorld world) {
		return this.worldEntityDAO.findOrCreateWorldEntityOf(world);
	}

}
