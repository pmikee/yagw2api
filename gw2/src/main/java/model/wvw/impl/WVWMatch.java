package model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Locale;

import model.IModelFactory;
import model.IWorld;
import model.wvw.IWVWMap;
import model.wvw.IWVWMatch;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWScores;
import model.wvw.types.IWVWObjective;
import utils.InjectionHelper;
import api.dto.IWVWMapDTO;
import api.dto.IWVWMatchDTO;
import api.dto.IWVWMatchDetailsDTO;
import api.dto.IWVWObjectiveDTO;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

public class WVWMatch implements IWVWMatch {
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);
	
	public static class WVWMatchBuilder implements IWVWMatch.IWVWMatchBuilder {
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
			final IWVWMatch match = new WVWMatch(this.id.get(), this.redWorld.get(), this.greenWorld.get(), this.blueWorld.get(),
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
				this.setupOwner(match, match.getCenterMap(), this.fromMatchDTO.get(), this.fromMatchDTO.get().getDetails().get().getCenterMap());
				this.setupOwner(match, match.getBlueMap(), this.fromMatchDTO.get(), this.fromMatchDTO.get().getDetails().get().getBlueMap());
				this.setupOwner(match, match.getRedMap(), this.fromMatchDTO.get(), this.fromMatchDTO.get().getDetails().get().getRedMap());
				this.setupOwner(match, match.getGreenMap(), this.fromMatchDTO.get(), this.fromMatchDTO.get().getDetails().get().getGreenMap());
			}
			return match;
		}

		private void setupOwner(IWVWMatch match, IWVWMap map, IWVWMatchDTO matchDTO, IWVWMapDTO mapDTO) {
			checkNotNull(match);
			checkNotNull(map);
			checkNotNull(matchDTO);
			Optional<IWVWObjective> objective;
			IWorld owner;
			for (IWVWObjectiveDTO objectiveDTO : mapDTO.getObjectives()) {
				if (objectiveDTO.getOwner() != null) {
					owner = match.getWorldByDTOOwnerString(objectiveDTO.getOwner());
					objective = map.getByObjectiveId(objectiveDTO.getId());
					checkState(objective.isPresent());
					objective.get().capture(owner);
				}
			}
		}

		@Override
		public IWVWMatch.IWVWMatchBuilder fromMatchDTO(IWVWMatchDTO dto, Locale locale) {
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

			final Optional<IWVWMatchDetailsDTO> details = dto.getDetails();
			if (details.isPresent()) {
				this.redScore(details.get().getRedScore());
				this.blueScore(details.get().getBlueScore());
				this.greenScore(details.get().getGreenScore());
			}
			return this;
		}

		@Override
		public IWVWMatch.IWVWMatchBuilder redScore(int score) {
			checkArgument(score > 0);
			this.redScore = Optional.of(score);
			return this;
		}

		@Override
		public IWVWMatch.IWVWMatchBuilder blueScore(int score) {
			checkArgument(score > 0);
			this.blueScore = Optional.of(score);
			return this;
		}

		@Override
		public IWVWMatch.IWVWMatchBuilder greenScore(int score) {
			checkArgument(score > 0);
			this.greenScore = Optional.of(score);
			return this;
		}

	}

	private final String id;
	private final IWorld redWorld;
	private final IWorld greenWorld;
	private final IWorld blueWorld;
	private final IWVWMap centerMap;
	private final IWVWMap redMap;
	private final IWVWMap greenMap;
	private final IWVWMap blueMap;
	private final IWVWScores scores;

	private WVWMatch(String id, IWorld redWorld, IWorld greenWorld, IWorld blueWorld, IWVWMap centerMap, IWVWMap redMap, IWVWMap greenMap, IWVWMap blueMap) {
		this.id = checkNotNull(id);
		this.redWorld = checkNotNull(redWorld);
		this.greenWorld = checkNotNull(greenWorld);
		this.blueWorld = checkNotNull(blueWorld);
		checkNotNull(centerMap);
		checkArgument(centerMap.getType().isCenter());
		this.centerMap = centerMap;
		checkNotNull(centerMap);
		checkArgument(redMap.getType().isRed());
		this.redMap = redMap;
		checkNotNull(greenMap);
		checkArgument(greenMap.getType().isGreen());
		this.greenMap = greenMap;
		checkNotNull(blueMap);
		checkArgument(blueMap.getType().isBlue());
		this.blueMap = blueMap;
		this.scores = WVW_MODEL_FACTORY.createScores();
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public IWorld[] getWorlds() {
		return new IWorld[] { this.redWorld, this.greenWorld, this.blueWorld };
	}

	@Override
	public IWorld getRedWOrld() {
		return this.redWorld;
	}

	@Override
	public IWorld getGreenWorld() {
		return this.greenWorld;
	}

	@Override
	public IWorld getBlueWorld() {
		return this.blueWorld;
	}

	@Override
	public IWVWMap getCenterMap() {
		return this.centerMap;
	}

	@Override
	public IWVWMap getBlueMap() {
		return this.blueMap;
	}

	@Override
	public IWVWMap getRedMap() {
		return this.redMap;
	}

	@Override
	public IWVWMap getGreenMap() {
		return this.greenMap;
	}

	@Override
	public IWVWScores getScores() {
		return this.scores;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("scores", this.scores).add("redWorld", this.redWorld).add("greenWorld", this.greenWorld)
				.add("blueWorld", this.blueWorld).add("centerMap", this.centerMap).add("redMap", this.redMap).add("greenMap", this.greenMap).add("blueMap", this.blueMap).toString();
	}

	@Override
	public IWorld getWorldByDTOOwnerString(String dtoOwnerString) {
		checkNotNull(dtoOwnerString);
		switch(dtoOwnerString.toUpperCase()) {
			case IWVWObjectiveDTO.OWNER_RED_STRING:
				return this.redWorld;
			case IWVWObjectiveDTO.OWNER_GREEN_STRING:
				return this.greenWorld;
			case IWVWObjectiveDTO.OWNER_BLUE_STRING:
				return this.blueWorld;
			default:
				throw new IllegalArgumentException("Invalid dtoOwnerString: "+dtoOwnerString);
		}
	}
}
