package model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Map;

import model.wvw.IHasWVWLocation;
import model.wvw.IWVWLocationType;
import model.wvw.IWVWMap;
import model.wvw.IWVWMapBuilder;
import model.wvw.IWVWMapType;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWObjective;
import utils.InjectionHelper;
import api.dto.IWVWMapDTO;
import api.dto.IWVWObjectiveDTO;

import com.google.common.base.Optional;

public class WVWMapBuilder implements IWVWMapBuilder {
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);

	private Map<IWVWLocationType, IHasWVWLocation> contentMappedByLocation = new HashMap<IWVWLocationType, IHasWVWLocation>();
	private Optional<IWVWMapType> type = Optional.absent();
	private Optional<Integer> redScore = Optional.absent();
	private Optional<Integer> blueScore = Optional.absent();
	private Optional<Integer> greenScore = Optional.absent();

	@Override
	public IWVWMap build() {
		checkState(WVWLocationType.forMapTyp(this.type.get()).isPresent());
		for (IWVWLocationType location : WVWLocationType.forMapTyp(this.type.get()).get()) {
			if (!this.contentMappedByLocation.containsKey(location)) {
				if (location.isObjectiveLocation()) {
					this.contentMappedByLocation.put(location, new WVWObjective(location));
				} else {
					// TODO
				}
			}
		}
		final IWVWMap map = WVW_MODEL_FACTORY.createMap(this.type.get(), this.contentMappedByLocation.values());
		if (this.redScore.isPresent()) {
			checkState(this.redScore.get() > 0);
			map.getScores().setRedScore(this.redScore.get());
		}
		if (this.greenScore.isPresent()) {
			checkState(this.greenScore.get() > 0);
			map.getScores().setGreenScore(this.greenScore.get());
		}
		if (this.blueScore.isPresent()) {
			checkState(this.blueScore.get() > 0);
			map.getScores().setBlueScore(this.blueScore.get());
		}
		return map;
	}

	@Override
	public IWVWMapBuilder type(IWVWMapType type) {
		this.type = Optional.fromNullable(type);
		return this;
	}

	@Override
	public IWVWMapBuilder objective(IWVWObjective objective) {
		checkNotNull(objective);
		checkNotNull(objective.getLocation());
		checkState(!this.contentMappedByLocation.containsKey(objective.getLocation()));
		this.contentMappedByLocation.put(objective.getLocation(), objective);
		return this;
	}

	@Override
	public IWVWMapBuilder fromDTO(IWVWMapDTO dto) {
		checkNotNull(dto);
		for (IWVWObjectiveDTO objectiveDTO : dto.getObjectives()) {
			this.objective(WVW_MODEL_FACTORY.createObjectiveBuilder().fromDTO(objectiveDTO).build());
		}
		this.type(WVW_MODEL_FACTORY.getMapTypeForDTOString(dto.getType()));		
		return this.blueScore(dto.getScores().getBlueScores()).redScore(dto.getScores().getRedScores()).greenScore(dto.getScores().getGreenScores());
	}

	@Override
	public IWVWMapBuilder redScore(int score) {
		checkArgument(score > 0);
		this.redScore = Optional.of(score);
		return this;
	}

	@Override
	public IWVWMapBuilder blueScore(int score) {
		checkArgument(score > 0);
		this.blueScore = Optional.of(score);
		return this;
	}

	@Override
	public IWVWMapBuilder greenScore(int score) {
		checkArgument(score > 0);
		this.greenScore = Optional.of(score);
		return this;
	}

}
