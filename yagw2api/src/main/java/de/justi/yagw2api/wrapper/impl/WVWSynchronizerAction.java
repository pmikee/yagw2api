package de.justi.yagw2api.wrapper.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import de.justi.yagw2api.api.dto.IWVWMapDTO;
import de.justi.yagw2api.api.dto.IWVWMatchDTO;
import de.justi.yagw2api.api.dto.IWVWMatchDetailsDTO;
import de.justi.yagw2api.api.dto.IWVWObjectiveDTO;
import de.justi.yagw2api.api.service.IWVWService;
import de.justi.yagw2api.utils.InjectionHelper;
import de.justi.yagw2api.wrapper.model.IWorld;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.wrapper.model.wvw.types.WVWMapType;

class WVWSynchronizerAction extends RecursiveAction{
	private static final long serialVersionUID = 8391498327079686666L;
	private static final int MAX_CHUNK_SIZE = 1;
	private static final Logger LOGGER = Logger.getLogger(WVWSynchronizerAction.class);
	private static final IWVWService SERVICE = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWService.class);

	private final int chunkSize;
	private final List<String> matchIds;
	private final int fromInclusive;
	private final int toExclusive;
	
	private final Map<String, IWVWMatch> matchesMappedById;

	public WVWSynchronizerAction(Map<String, IWVWMatch> matchesMappedById) {
		this(ImmutableMap.copyOf(matchesMappedById), ImmutableList.copyOf(matchesMappedById.keySet()), MAX_CHUNK_SIZE, 0, matchesMappedById.size());
	}

	private WVWSynchronizerAction(Map<String, IWVWMatch> matchesMappedById, List<String> matchIds, int chunkSize, int fromInclusive, int toExclusive) {
		checkArgument(chunkSize > 0);
		checkNotNull(matchIds);
		checkArgument(fromInclusive >= 0);
		checkArgument(fromInclusive <= toExclusive);
		this.chunkSize = chunkSize;
		this.matchIds = matchIds;
		this.fromInclusive = fromInclusive;
		this.toExclusive = toExclusive;
		this.matchesMappedById = matchesMappedById;
		LOGGER.trace("New " + this.getClass().getSimpleName() + " that handles match ids " + this.matchIds.subList(this.fromInclusive, this.toExclusive));
	}
	
	@Override
	protected final void compute() {
		try {
			if (this.toExclusive - this.fromInclusive <= this.chunkSize) {
				// compute directly
				for (int index = this.fromInclusive; index < this.toExclusive; index++) {
					this.perform(this.matchIds.get(index));
				}
				return;
			} else {
				// fork
				final int splitAtIndex = this.fromInclusive + ((this.toExclusive - this.fromInclusive) / 2);
				invokeAll(this.createSubTask(this.matchIds, this.chunkSize, this.fromInclusive, splitAtIndex), this.createSubTask(this.matchIds, this.chunkSize, splitAtIndex,
						this.toExclusive));
			}
		} catch (Exception e) {
			LOGGER.fatal("Failed during execution of "+this.getClass().getSimpleName()+" computing "+this.fromInclusive+"-"+this.toExclusive,e);
		}
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
		IWorld potentialNewOwner;
		for (IWVWObjectiveDTO objectiveDTO : mapDTO.getObjectives()) {
			optionalObjectiveModel = mapModel.getByObjectiveId(objectiveDTO.getId());
			if (optionalObjectiveModel.isPresent()) {
				objectiveModel = optionalObjectiveModel.get();
				objectiveModel.updateOnSynchronization();
				if (LOGGER.isTraceEnabled()) {
					LOGGER.trace("Going to synchronize model=" + objectiveModel + " with dto=" + objectiveDTO);
				}
				if (objectiveDTO.getOwner() != null) {
					potentialNewOwner = match.getWorldByDTOOwnerString(objectiveDTO.getOwner()).get();
					if (objectiveModel.getOwner().isPresent()) {
						// check for owner change
						if (!potentialNewOwner.equals(objectiveModel.getOwner().get())) {
							// -> owner change
							if(LOGGER.isDebugEnabled()) {
								LOGGER.debug("Detected an owner change of " + objectiveModel + " from " + objectiveModel.getOwner() + " to " + potentialNewOwner);
							}
							objectiveModel.capture(potentialNewOwner);
						}else{
							// -> no owner change at all
							if(LOGGER.isTraceEnabled()) {
								LOGGER.trace("Owner of " + objectiveModel + " has not changed.");
							}
						}
					} else {
						// first capture
						if(LOGGER.isDebugEnabled()) {
							LOGGER.debug("Detected first capture of " + objectiveModel + " by " + potentialNewOwner);
						}
						objectiveModel.capture(potentialNewOwner);
					}
				} else {
					// no owner
					if(objectiveModel.getOwner().isPresent()) {
						// TODO unset owner of objective -> can happen after reset!?
					}else {
						// still not captured --> nothing to do
						if(LOGGER.isTraceEnabled()) {
							LOGGER.trace(objectiveModel + " has still been not captured yet.");
						}
					}
				}
			} else {
				LOGGER.error("Missing " + IWVWObjective.class.getSimpleName() + " for objectiveId=" + objectiveDTO.getId());
			}
		}
		
		// 2. synchronize scores
		mapModel.getScores().update(mapDTO.getRedScore(), mapDTO.getGreenScore(), mapDTO.getBlueScore());
	}
}
