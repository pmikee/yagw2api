package model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Locale;

import model.IModelFactory;
import model.IWorld;
import model.wvw.IWVWMap;
import model.wvw.IWVWMatch;
import model.wvw.IWVWMatchBuilder;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWObjective;
import utils.InjectionHelper;
import api.dto.IWVWMatchDTO;
import api.dto.IWVWObjectiveDTO;

import com.google.common.base.Optional;

public class WVWMatchBuilder implements IWVWMatchBuilder {
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);
	private static final IModelFactory MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IModelFactory.class);

	private Optional<IWVWMatchDTO> fromMatchDTO = Optional.absent();
	private Optional<String> id = Optional.absent();
	private Optional<IWVWMap> centerMap = Optional.absent();
	private Optional<IWVWMap> redMap = Optional.absent();
	private Optional<IWVWMap> greenMap = Optional.absent();
	private Optional<IWVWMap> blueMap = Optional.absent();
	private Optional<IWorld> redWorld = Optional.absent();
	private Optional<IWorld> greenWorld = Optional.absent();
	private Optional<IWorld> blueWorld = Optional.absent();
	private Optional<Integer> redScore = Optional.absent();
	private Optional<Integer> greenScore = Optional.absent();
	private Optional<Integer> blueScore = Optional.absent();

	@Override
	public IWVWMatch build() {
		final IWVWMatch match = WVW_MODEL_FACTORY.createWVWMatch(this.id.get(), this.redWorld.get(), this.greenWorld.get(), this.blueWorld.get(),
				this.centerMap.get(), this.redMap.get(), this.greenMap.get(), this.blueMap.get());
		if (this.redScore.isPresent()) {
			checkState(this.redScore.get() > 0);
			match.getScores().setRedScore(this.redScore.get());
		}
		if (this.greenScore.isPresent()) {
			checkState(greenScore.get() > 0);
			match.getScores().setGreenScore(this.greenScore.get());
		}
		if (this.blueScore.isPresent()) {
			checkState(this.blueScore.get() > 0);
			match.getScores().setBlueScore(this.blueScore.get());
		}
		if (this.fromMatchDTO.isPresent()) {
			checkState(this.fromMatchDTO.get().getDetails().isPresent());
			Optional<IWVWObjective> objective;
			IWorld owner;
			for (IWVWObjectiveDTO objectiveDTO : this.fromMatchDTO.get().getDetails().get().getCenterMap().getObjectives()) {
				if (objectiveDTO.getOwner() != null) {
					owner = match.getWorldByDTOOwnerString(objectiveDTO.getOwner());
					objective = match.getCenterMap().getByObjectiveId(objectiveDTO.getId());
					checkState(objective.isPresent());
					objective.get().capture(owner);
				}
			}
			for (IWVWObjectiveDTO objectiveDTO : this.fromMatchDTO.get().getDetails().get().getRedMap().getObjectives()) {
				if (objectiveDTO.getOwner() != null) {
					owner = match.getWorldByDTOOwnerString(objectiveDTO.getOwner());
					objective = match.getRedMap().getByObjectiveId(objectiveDTO.getId());
					checkState(objective.isPresent());
					objective.get().capture(owner);
				}
			}
			for (IWVWObjectiveDTO objectiveDTO : this.fromMatchDTO.get().getDetails().get().getGreenMap().getObjectives()) {
				if (objectiveDTO.getOwner() != null) {
					owner = match.getWorldByDTOOwnerString(objectiveDTO.getOwner());
					objective = match.getGreenMap().getByObjectiveId(objectiveDTO.getId());
					checkState(objective.isPresent());
					objective.get().capture(owner);
				}
			}
			for (IWVWObjectiveDTO objectiveDTO : this.fromMatchDTO.get().getDetails().get().getBlueMap().getObjectives()) {
				if (objectiveDTO.getOwner() != null) {
					owner = match.getWorldByDTOOwnerString(objectiveDTO.getOwner());
					objective = match.getBlueMap().getByObjectiveId(objectiveDTO.getId());
					checkState(objective.isPresent());
					objective.get().capture(owner);
				}
			}
		}
		return match;
	}

	@Override
	public IWVWMatchBuilder fromMatchDTO(IWVWMatchDTO dto, Locale locale) {
		checkNotNull(locale);
		checkState(!this.fromMatchDTO.isPresent());
		this.fromMatchDTO = Optional.of(dto);
		this.id = Optional.of(dto.getId());
		this.centerMap = Optional.of(WVW_MODEL_FACTORY.createMapBuilder().fromDTO(dto.getDetails().get().getCenterMap()).build());
		checkState(this.centerMap.get().getType().isCenter());
		this.redMap = Optional.of(WVW_MODEL_FACTORY.createMapBuilder().fromDTO(dto.getDetails().get().getRedMap()).build());
		checkState(this.redMap.get().getType().isRed());
		this.greenMap = Optional.of(WVW_MODEL_FACTORY.createMapBuilder().fromDTO(dto.getDetails().get().getGreenMap()).build());
		checkState(this.greenMap.get().getType().isGreen());
		this.blueMap = Optional.of(WVW_MODEL_FACTORY.createMapBuilder().fromDTO(dto.getDetails().get().getBlueMap()).build());
		checkState(this.blueMap.get().getType().isBlue());
		this.redWorld = Optional.of(MODEL_FACTORY.createWorld(dto.getRedWorldId(), dto.getRedWorldName(locale).get().getName()));
		this.greenWorld = Optional.of(MODEL_FACTORY.createWorld(dto.getGreenWorldId(), dto.getGreenWorldName(locale).get().getName()));
		this.blueWorld = Optional.of(MODEL_FACTORY.createWorld(dto.getBlueWorldId(), dto.getGreenWorldName(locale).get().getName()));
		return this;
	}

	@Override
	public IWVWMatchBuilder redScore(int score) {
		checkArgument(score > 0);
		this.redScore = Optional.of(score);
		return this;
	}

	@Override
	public IWVWMatchBuilder blueScore(int score) {
		checkArgument(score > 0);
		this.blueScore = Optional.of(score);
		return this;
	}

	@Override
	public IWVWMatchBuilder greenScore(int score) {
		checkArgument(score > 0);
		this.greenScore = Optional.of(score);
		return this;
	}

}
