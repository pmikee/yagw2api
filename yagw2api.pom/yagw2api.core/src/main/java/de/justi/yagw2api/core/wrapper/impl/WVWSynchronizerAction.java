package de.justi.yagw2api.core.wrapper.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import de.justi.yagw2api.core.YAGW2APICore;
import de.justi.yagw2api.core.arenanet.dto.IGuildDetailsDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMapDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDetailsDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWObjectiveDTO;
import de.justi.yagw2api.core.arenanet.service.IWVWService;
import de.justi.yagw2api.core.wrapper.model.IGuild;
import de.justi.yagw2api.core.wrapper.model.IModelFactory;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.core.wrapper.model.wvw.types.WVWMapType;

class WVWSynchronizerAction extends AbstractSynchronizerAction<String,WVWSynchronizerAction>{
	private static final long serialVersionUID = 8391498327079686666L;
	private static final int MAX_CHUNK_SIZE = 1;
	private static final Logger LOGGER = Logger.getLogger(WVWSynchronizerAction.class);
	private static final IWVWService SERVICE = YAGW2APICore.getInjector().getInstance(IWVWService.class);
	private static final IModelFactory MODEL_FACTORY = YAGW2APICore.getInjector().getInstance(IModelFactory.class);

	
	private final Map<String, IWVWMatch> matchesMappedById;

	public WVWSynchronizerAction(Map<String, IWVWMatch> matchesMappedById) {
		this(ImmutableMap.copyOf(matchesMappedById), ImmutableList.copyOf(matchesMappedById.keySet()), MAX_CHUNK_SIZE, 0, matchesMappedById.size());
	}

	private WVWSynchronizerAction(Map<String, IWVWMatch> matchesMappedById, List<String> matchIds, int chunkSize, int fromInclusive, int toExclusive) {
		super(matchIds, chunkSize, fromInclusive, toExclusive);
		checkArgument(chunkSize > 0);
		checkNotNull(matchIds);
		checkArgument(fromInclusive >= 0);
		checkArgument(fromInclusive <= toExclusive);
		this.matchesMappedById = matchesMappedById;
	}
	protected WVWSynchronizerAction createSubTask(List<String> mapIds, int chunkSize, int fromInclusive, int toExclusive) {
		return new WVWSynchronizerAction(this.matchesMappedById, mapIds, chunkSize, fromInclusive, toExclusive);
	}

	protected void perform(String matchId) {
		final long startTimestamp = System.currentTimeMillis();
		LOGGER.trace("Going to synchronize matchId=" + matchId);
		final Optional<IWVWMatchDTO> matchDTOOptional = SERVICE.retrieveMatch(matchId);
		if (matchDTOOptional.isPresent() && matchDTOOptional.get().getDetails().isPresent()) {
			final IWVWMatchDTO matchDTO = matchDTOOptional.get();
			final IWVWMatchDetailsDTO matchDetailsDTO = matchDTO.getDetails().get();
			final IWVWMatch matchModel = this.matchesMappedById.get(matchId);
			
			// 1. synchronize maps
			this.synchronizeMap(matchModel, matchModel.getCenterMap(), matchDetailsDTO.getCenterMap());
			this.synchronizeMap(matchModel, matchModel.getRedMap(), matchDetailsDTO.getRedMap());
			this.synchronizeMap(matchModel, matchModel.getBlueMap(), matchDetailsDTO.getBlueMap());
			this.synchronizeMap(matchModel, matchModel.getGreenMap(), matchDetailsDTO.getGreenMap());
			// 2. synchronize match scores
			matchModel.getScores().update(matchDetailsDTO.getRedScore(), matchDetailsDTO.getGreenScore(), matchDetailsDTO.getBlueScore());
		} else {
			LOGGER.error("Failed to retrieve " + IWVWMatchDTO.class.getSimpleName() + " for matchId=" + matchId);
		}
		final long endTimestamp = System.currentTimeMillis();
		final long duration = endTimestamp-startTimestamp;
		LOGGER.info("Synchronized matchId=" + matchId+" in "+duration+"ms.");
	}

	private void synchronizeMap(IWVWMatch match, IWVWMap mapModel, IWVWMapDTO mapDTO) {
		checkNotNull(mapDTO);
		checkNotNull(mapModel);
		
		checkArgument(WVWMapType.fromDTOString(mapDTO.getType()).equals(mapModel.getType()));
		
		checkArgument(mapDTO.getRedScore() >= 0);
		checkArgument(mapDTO.getBlueScore() >= 0);
		checkArgument(mapDTO.getGreenScore() >= 0);
		checkArgument(mapDTO.getObjectives().length == mapModel.getObjectives().size());
		
		// 1. synchronize objectives
		Optional<IWVWObjective> optionalObjectiveModel;
		IWVWObjective objectiveModel;
		Optional<IWorld> potentialNewOwner;
		Optional<IGuildDetailsDTO> claimedByGuildDTO;
		for (IWVWObjectiveDTO objectiveDTO : mapDTO.getObjectives()) {
			optionalObjectiveModel = mapModel.getByObjectiveId(objectiveDTO.getId());
			if (optionalObjectiveModel.isPresent()) {
				objectiveModel = optionalObjectiveModel.get();
				objectiveModel.updateOnSynchronization();
				if (LOGGER.isTraceEnabled()) {
					LOGGER.trace("Going to synchronize model=" + objectiveModel + " with dto=" + objectiveDTO);
				}
				
				// 1.1 synchronize owner
				potentialNewOwner = match.getWorldByDTOOwnerString(objectiveDTO.getOwner());
				objectiveModel.capture(potentialNewOwner.orNull());
				// 1.2 synchronize claiming guild
				claimedByGuildDTO = objectiveDTO.getGuildDetails();
				if(claimedByGuildDTO.isPresent()) {
					final IGuild guild = MODEL_FACTORY.getOrCreateGuild(claimedByGuildDTO.get().getId(), claimedByGuildDTO.get().getName(), claimedByGuildDTO.get().getTag());
					objectiveModel.claim(guild);
				}else {
					objectiveModel.claim(null);
				}
			} else {
				LOGGER.error("Missing " + IWVWObjective.class.getSimpleName() + " for objectiveId=" + objectiveDTO.getId());
			}
		}
		
		// 2. synchronize scores
		mapModel.getScores().update(mapDTO.getRedScore(), mapDTO.getGreenScore(), mapDTO.getBlueScore());
	}
}
