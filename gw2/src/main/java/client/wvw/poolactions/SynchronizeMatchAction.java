package client.wvw.poolactions;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import model.IModelFactory;
import model.IWorld;
import model.wvw.IWVWMap;
import model.wvw.IWVWMatch;
import model.wvw.IWVWModelFactory;
import model.wvw.types.IWVWObjective;

import org.apache.log4j.Logger;

import utils.InjectionHelper;
import api.dto.IWVWMapDTO;
import api.dto.IWVWMatchDTO;
import api.dto.IWVWMatchDetailsDTO;
import api.dto.IWVWObjectiveDTO;
import api.service.IWVWService;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class SynchronizeMatchAction extends AbstractMatchIdAction<SynchronizeMatchAction> {
	private static final long serialVersionUID = 8391498327079686666L;
	private static final int MAX_CHUNK_SIZE = 2;
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
			this.synchronizeMap(matchModel, matchModel.getCenterMap(), matchDetailsDTO.getCenterMap());
			this.synchronizeMap(matchModel, matchModel.getRedMap(), matchDetailsDTO.getRedMap());
			this.synchronizeMap(matchModel, matchModel.getBlueMap(), matchDetailsDTO.getBlueMap());
			this.synchronizeMap(matchModel, matchModel.getGreenMap(), matchDetailsDTO.getGreenMap());

		} else {
			LOGGER.error("Failed to retrieve " + IWVWMatchDTO.class.getSimpleName() + " for matchId=" + matchId);
		}

		LOGGER.trace("Synchronized matchId=" + matchId);
	}

	private void synchronizeMap(IWVWMatch match, IWVWMap mapModel, IWVWMapDTO mapDTO) {
		checkNotNull(mapDTO);
		checkNotNull(mapModel);
		
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
		//TODO synchronize scores
	}
}
