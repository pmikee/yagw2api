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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

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
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.domain.DomainFactory;
import de.justi.yagw2api.wrapper.domain.guild.Guild;
import de.justi.yagw2api.wrapper.domain.world.World;
import de.justi.yagw2api.wrapper.domain.wvw.WVWMap;
import de.justi.yagw2api.wrapper.domain.wvw.WVWMatch;
import de.justi.yagw2api.wrapper.domain.wvw.WVWObjective;
import de.justi.yagw2api.wrapper.domain.wvw.DefaultWVWMapType;

final class WVWSynchronizerAction extends AbstractSynchronizerAction<String, WVWSynchronizerAction> {
	private static final long serialVersionUID = 8391498327079686666L;
	private static final int MAX_CHUNK_SIZE = 1;
	private static final Logger LOGGER = LoggerFactory.getLogger(WVWSynchronizerAction.class);
	private static final WVWService WVW_SERVICE = YAGW2APIArenanet.INSTANCE.getWVWService();
	private static final DomainFactory MODEL_FACTORY = YAGW2APIWrapper.INSTANCE.getDomainFactory();

	private final Map<String, WVWMatch> matchesMappedById;

	public WVWSynchronizerAction(final Map<String, WVWMatch> matchesMappedById) {
		this(ImmutableMap.copyOf(matchesMappedById), ImmutableList.copyOf(matchesMappedById.keySet()), MAX_CHUNK_SIZE, 0, matchesMappedById.size());
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Created new " + this.getClass().getSimpleName() + " that has to handle " + this.matchesMappedById);
		}
	}

	private WVWSynchronizerAction(final Map<String, WVWMatch> matchesMappedById, final List<String> matchIds, final int chunkSize, final int fromInclusive, final int toExclusive) {
		super(matchIds, chunkSize, fromInclusive, toExclusive);
		checkArgument(chunkSize > 0);
		checkNotNull(matchIds);
		checkArgument(fromInclusive >= 0);
		checkArgument(fromInclusive <= toExclusive);
		this.matchesMappedById = matchesMappedById;
	}

	@Override
	protected WVWSynchronizerAction createSubTask(final List<String> mapIds, final int chunkSize, final int fromInclusive, final int toExclusive) {
		return new WVWSynchronizerAction(this.matchesMappedById, mapIds, chunkSize, fromInclusive, toExclusive);
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
					final Guild guild = MODEL_FACTORY.getOrCreateGuild(claimedByGuildDTO.get().getId(), claimedByGuildDTO.get().getName(), claimedByGuildDTO.get().getTag());
					objectiveModel.claim(guild);
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
