package de.justi.gw2.client.wvw.poolactions;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import de.justi.gw2.api.dto.IWVWMapDTO;
import de.justi.gw2.api.dto.IWVWMatchDTO;
import de.justi.gw2.api.dto.IWVWMatchDetailsDTO;
import de.justi.gw2.api.dto.IWVWObjectiveDTO;
import de.justi.gw2.api.service.IWVWService;
import de.justi.gw2.model.IModelFactory;
import de.justi.gw2.model.IWorld;
import de.justi.gw2.model.wvw.IWVWMap;
import de.justi.gw2.model.wvw.IWVWMatch;
import de.justi.gw2.model.wvw.IWVWModelFactory;
import de.justi.gw2.model.wvw.IWVWObjective;
import de.justi.gw2.model.wvw.types.impl.WVWMapType;
import de.justi.gw2.utils.InjectionHelper;

public class SynchronizeMatchAction extends AbstractMatchIdAction<SynchronizeMatchAction> {
	private static final long serialVersionUID = 8391498327079686666L;
	private static final int MAX_CHUNK_SIZE = 1;
	private static final Logger LOGGER = Logger.getLogger(SynchronizeMatchAction.class);
	private static final IWVWService SERVICE = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWService.class);
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);
	private static final IModelFactory MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IModelFactory.class);

	private final Map<String, IWVWMatch> matchesMappedById;

	public SynchronizeMatchAction(Map<String, IWVWMatch> matchesMappedById) {
		this(ImmutableMap.copyOf(matchesMappedById), matchesMappedById.keySet(), MAX_CHUNK_SIZE, 0, matchesMappedById.size());
	}

	private SynchronizeMatchAction(Map<String, IWVWMatch> matchesMappedById, Collection<String> matchIds, int chunkSize, int fromInclusive, int toExclusive) {
		super(ImmutableList.copyOf(matchIds), chunkSize, fromInclusive, toExclusive);
		this.matchesMappedById = matchesMappedById;
	}

	@Override
	protected SynchronizeMatchAction createSubTask(List<String> mapIds, int chunkSize, int fromInclusive, int toExclusive) {
		return new SynchronizeMatchAction(this.matchesMappedById, mapIds, chunkSize, fromInclusive, toExclusive);
	}

	@Override
	protected void perform(String matchId) {
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

		LOGGER.trace("Synchronized matchId=" + matchId);
	}

	private void synchronizeMap(IWVWMatch match, IWVWMap mapModel, IWVWMapDTO mapDTO) {
		checkNotNull(mapDTO);
		checkNotNull(mapModel);
		
		checkArgument(WVWMapType.fromDTOString(mapDTO.getType()).equals(mapModel.getType()));
		
		checkArgument(mapDTO.getRedScore() >= 0);
		checkArgument(mapDTO.getBlueScore() >= 0);
		checkArgument(mapDTO.getGreenScore() >= 0);
		
		// 1. synchronize objectives
		Optional<IWVWObjective> optionalObjectiveModel;
		IWVWObjective objectiveModel;
		IWorld potentialNewOwner;
		for (IWVWObjectiveDTO objectiveDTO : mapDTO.getObjectives()) {
			optionalObjectiveModel = mapModel.getByObjectiveId(objectiveDTO.getId());
			if (optionalObjectiveModel.isPresent()) {
				objectiveModel = optionalObjectiveModel.get();
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
