package de.justi.yagw2api.wrapper.wvw;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.WVWService;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.arenanet.dto.guild.GuildDetailsDTO;
import de.justi.yagw2api.arenanet.dto.world.WorldNameDTO;
import de.justi.yagw2api.arenanet.dto.wvw.WVWMapDTO;
import de.justi.yagw2api.arenanet.dto.wvw.WVWMatchDTO;
import de.justi.yagw2api.arenanet.dto.wvw.WVWMatchDetailsDTO;
import de.justi.yagw2api.arenanet.dto.wvw.WVWObjectiveDTO;
import de.justi.yagw2api.arenanet.dto.wvw.WVWObjectiveNameDTO;
import de.justi.yagw2api.wrapper.AbstractSynchronizerAction;
import de.justi.yagw2api.wrapper.guild.GuildWrapper;
import de.justi.yagw2api.wrapper.guild.domain.Guild;
import de.justi.yagw2api.wrapper.guild.domain.NoSuchGuildException;
import de.justi.yagw2api.wrapper.world.domain.World;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMap;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMatch;
import de.justi.yagw2api.wrapper.wvw.domain.WVWObjective;
import de.justi.yagw2api.wrapper.wvw.domain.impl.DefaultWVWMapType;

final class WVWSynchronizerAction extends AbstractSynchronizerAction<String, WVWSynchronizerAction> {
	// CONSTS
	private static final long serialVersionUID = 8391498327079686666L;
	private static final int MAX_CHUNK_SIZE = 1;
	private static final Logger LOGGER = LoggerFactory.getLogger(WVWSynchronizerAction.class);
	private static final WVWService WVW_SERVICE = YAGW2APIArenanet.INSTANCE.getWVWService();

	// FIELDS
	private final Map<String, WVWMatch> matchesMappedById;
	private final GuildWrapper guildWrapper;

	// CONSTRUCTOR
	@Inject
	public WVWSynchronizerAction(final GuildWrapper guildWrapper, final Map<String, WVWMatch> matchesMappedById) {
		this(checkNotNull(guildWrapper, "missing guildWrapper"), ImmutableMap.copyOf(matchesMappedById), ImmutableList.copyOf(matchesMappedById.keySet()), MAX_CHUNK_SIZE, 0,
				matchesMappedById.size());
		LOGGER.trace("Created new {} that has to handle {}", this.getClass(), this.matchesMappedById);
	}

	@Inject
	private WVWSynchronizerAction(final GuildWrapper guildWrapper, final Map<String, WVWMatch> matchesMappedById, final List<String> matchIds, final int chunkSize,
			final int fromInclusive, final int toExclusive) {
		super(matchIds, chunkSize, fromInclusive, toExclusive);
		checkArgument(chunkSize > 0);
		checkNotNull(matchIds);
		checkArgument(fromInclusive >= 0);
		checkArgument(fromInclusive <= toExclusive);
		this.matchesMappedById = matchesMappedById;
		this.guildWrapper = checkNotNull(guildWrapper, "missing guildWrapper");
	}

	// METHODS
	@Override
	protected WVWSynchronizerAction createSubTask(final List<String> mapIds, final int chunkSize, final int fromInclusive, final int toExclusive) {
		return new WVWSynchronizerAction(this.guildWrapper, this.matchesMappedById, mapIds, chunkSize, fromInclusive, toExclusive);
	}

	@Override
	protected void perform(final String matchId) {
		final long startTimestamp = System.currentTimeMillis();
		final Optional<WVWMatchDTO> matchDTOOptional = WVW_SERVICE.retrieveMatch(matchId);
		if (matchDTOOptional.isPresent() && matchDTOOptional.get().getDetails().isPresent()) {
			final WVWMatchDTO matchDTO = matchDTOOptional.get();
			final WVWMatchDetailsDTO matchDetailsDTO = matchDTO.getDetails().get();
			final WVWMatch matchModel = this.matchesMappedById.get(matchId);

			// 0. synchronize worlds
			this.synchronizeWorldNames(matchModel, matchDTO);

			// 1. synchronize maps
			this.synchronizeMap(matchModel, matchModel.getCenterMap(), matchDetailsDTO.getCenterMap());
			this.synchronizeMap(matchModel, matchModel.getRedMap(), matchDetailsDTO.getRedMap());
			this.synchronizeMap(matchModel, matchModel.getBlueMap(), matchDetailsDTO.getBlueMap());
			this.synchronizeMap(matchModel, matchModel.getGreenMap(), matchDetailsDTO.getGreenMap());
			// 2. synchronize match scores
			matchModel.getScores().update(matchDetailsDTO.getRedScore(), matchDetailsDTO.getGreenScore(), matchDetailsDTO.getBlueScore());
		} else {
			LOGGER.error("Failed to retrieve " + WVWMatchDTO.class.getSimpleName() + " for matchId=" + matchId);
		}
		final long endTimestamp = System.currentTimeMillis();
		final long duration = endTimestamp - startTimestamp;
		LOGGER.trace("Synchronized matchId=" + matchId + " in " + duration + "ms.");
	}

	private void synchronizeWorldNames(final WVWMatch matchModel, final WVWMatchDTO matchDTO) {
		final Optional<WorldNameDTO> greenName = matchDTO.getGreenWorldName(YAGW2APIArenanet.INSTANCE.getCurrentLocale());
		final Optional<WorldNameDTO> redName = matchDTO.getRedWorldName(YAGW2APIArenanet.INSTANCE.getCurrentLocale());
		final Optional<WorldNameDTO> blueName = matchDTO.getBlueWorldName(YAGW2APIArenanet.INSTANCE.getCurrentLocale());
		if (greenName.isPresent()) {
			matchModel.getGreenWorld().setName(greenName.get().getNameWithoutLocale());
		}
		if (redName.isPresent()) {
			matchModel.getRedWorld().setName(redName.get().getNameWithoutLocale());
		}
		if (blueName.isPresent()) {
			matchModel.getBlueWorld().setName(blueName.get().getNameWithoutLocale());
		}
	}

	private void synchronizeMap(final WVWMatch match, final WVWMap mapModel, final WVWMapDTO mapDTO) {
		checkNotNull(mapDTO);
		checkNotNull(mapModel);

		checkArgument(DefaultWVWMapType.fromDTOString(mapDTO.getType()).equals(mapModel.getType()));

		checkArgument(mapDTO.getRedScore() >= 0);
		checkArgument(mapDTO.getBlueScore() >= 0);
		checkArgument(mapDTO.getGreenScore() >= 0);
		checkArgument(mapDTO.getObjectives().length == mapModel.getObjectives().size());

		// 1. synchronize objectives
		Optional<WVWObjective> optionalObjectiveModel;
		WVWObjective objectiveModel;
		Optional<World> potentialNewOwner;
		Optional<GuildDetailsDTO> claimedByGuildDTO;
		for (WVWObjectiveDTO objectiveDTO : mapDTO.getObjectives()) {
			optionalObjectiveModel = mapModel.getByObjectiveId(objectiveDTO.getId());
			if (optionalObjectiveModel.isPresent()) {
				objectiveModel = optionalObjectiveModel.get();
				objectiveModel.updateOnSynchronization();

				// 1.1 synchronize owner
				potentialNewOwner = match.getWorldByDTOOwnerString(objectiveDTO.getOwner());
				objectiveModel.capture(potentialNewOwner.orNull());
				// 1.2 synchronize claiming guild
				claimedByGuildDTO = objectiveDTO.getGuildDetails();
				if (claimedByGuildDTO.isPresent()) {
					try {
						final Guild guild = this.guildWrapper.getGuild(claimedByGuildDTO.get().getId());
						objectiveModel.claim(guild);
					} catch (NoSuchGuildException e) {
						Throwables.propagate(e);
					}
				} else {
					objectiveModel.claim(null);
				}
			} else {
				LOGGER.error("Missing {} for objectiveId={}", WVWObjectiveNameDTO.class, objectiveDTO.getId());
			}
		}

		// 2. synchronize scores
		mapModel.getScores().update(mapDTO.getRedScore(), mapDTO.getGreenScore(), mapDTO.getBlueScore());
	}
}
