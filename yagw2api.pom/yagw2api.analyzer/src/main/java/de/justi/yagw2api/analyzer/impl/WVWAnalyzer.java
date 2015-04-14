package de.justi.yagw2api.analyzer.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.IWVWMatchEntityDAO;
import de.justi.yagw2api.analyzer.IWorldEnityDAO;
import de.justi.yagw2api.analyzer.IWorldEntity;
import de.justi.yagw2api.wrapper.domain.world.World;
import de.justi.yagw2api.wrapper.domain.wvw.WVWMatch;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWObjectiveUnclaimedEvent;

final class WVWAnalyzer implements IWVWAnalyzer {
	private static final Logger LOGGER = LoggerFactory.getLogger(WVWAnalyzer.class);
	@Inject
	private IWorldEnityDAO worldEntityDAO;
	@Inject
	private IWVWMatchEntityDAO wvwMatchEntityDAO;

	@Override
	public void onMatchScoreChangedEvent(final WVWMatchScoresChangedEvent event) {
		LOGGER.debug("{}", event);
		this.synchronizeWorldsOfMatch(event.getTimestamp(), event.getMatch());
	}

	@Override
	public void onObjectiveCapturedEvent(final WVWObjectiveCaptureEvent event) {
		LOGGER.debug("{}", event);
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getTimestamp(), event.getMap().getMatch().get());
		}
	}

	@Override
	public void onObjectiveEndOfBuffEvent(final WVWObjectiveEndOfBuffEvent event) {
		LOGGER.debug("{}", event);
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getTimestamp(), event.getMap().getMatch().get());
		}
	}

	@Override
	public void onObjectiveClaimedEvent(final WVWObjectiveClaimedEvent event) {
		LOGGER.debug("{}", event);
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getTimestamp(), event.getMap().getMatch().get());
		}
	}

	@Override
	public void onObjectiveUnclaimedEvent(final WVWObjectiveUnclaimedEvent event) {
		LOGGER.debug("{}", event);
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getTimestamp(), event.getMap().getMatch().get());
		}
	}

	@Override
	public void onInitializedMatchForWrapper(final WVWInitializedMatchEvent event) {
		LOGGER.debug("{}", event);
		this.synchronizeWorldsOfMatch(event.getTimestamp(), event.getMatch());
	}

	@Override
	public void onChangedMapScoreEvent(final WVWMapScoresChangedEvent event) {
		LOGGER.debug("{}", event);
		if (event.getMap().getMatch().isPresent()) {
			this.synchronizeWorldsOfMatch(event.getTimestamp(), event.getMap().getMatch().get());
		}
	}

	private boolean synchronizeWorldsOfMatch(final LocalDateTime timestamp, final WVWMatch match) {
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

		LOGGER.trace("Successfully synchronized {}", entity);
		persisted = this.wvwMatchEntityDAO.save(entity);
		if (persisted) {
			LOGGER.trace("Successfully persisted {}", entity);
		} else {
			LOGGER.error("Failed to persist {}", entity);
		}
		return persisted;
	}

	@Override
	public IWorldEntity worldEntityOf(final World world) {
		return this.worldEntityDAO.findOrCreateWorldEntityOf(world);
	}

}
