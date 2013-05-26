package model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import model.AbstractHasChannel;
import model.IModelFactory;
import model.IWorld;
import model.wvw.IWVWMap;
import model.wvw.IWVWMatch;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWScores;
import model.wvw.types.IWVWObjective;

import org.apache.log4j.Logger;

import utils.InjectionHelper;
import api.dto.IWVWMapDTO;
import api.dto.IWVWMatchDTO;
import api.dto.IWVWMatchDetailsDTO;
import api.dto.IWVWObjectiveDTO;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;

class WVWMatch extends AbstractHasChannel implements IWVWMatch {

	class WVWImmutableMatchDecorator implements IWVWMatch {

		@Override
		public Set<IWorld> searchWorldsByNamePattern(Pattern searchPattern) {
			final Collection<IWorld> result = new ArrayList<IWorld>();
			for (IWorld world : WVWMatch.this.searchWorldsByNamePattern(searchPattern)) {
				result.add(world.createImmutableReference());
			}
			return ImmutableSet.copyOf(result);
		}

		@Override
		public String getId() {
			return WVWMatch.this.getId();
		}

		@Override
		public IWorld[] getWorlds() {
			final java.util.List<IWorld> buffer = new ArrayList<IWorld>();
			for (IWorld world : WVWMatch.this.getWorlds()) {
				buffer.add(world.createImmutableReference());
			}
			return buffer.toArray(new IWorld[0]);
		}

		@Override
		public IWorld getRedWorld() {
			return WVWMatch.this.getRedWorld().createImmutableReference();
		}

		@Override
		public IWorld getGreenWorld() {
			return WVWMatch.this.getGreenWorld().createImmutableReference();
		}

		@Override
		public IWorld getBlueWorld() {
			return WVWMatch.this.getBlueWorld().createImmutableReference();
		}

		@Override
		public Optional<IWorld> getWorldByDTOOwnerString(String dtoOwnerString) {
			final Optional<IWorld> buffer = WVWMatch.this.getWorldByDTOOwnerString(dtoOwnerString);
			return buffer.isPresent() ? Optional.of(buffer.get().createImmutableReference()) : buffer;
		}

		@Override
		public IWVWMap getCenterMap() {
			return WVWMatch.this.getCenterMap().createImmutableReference();
		}

		@Override
		public IWVWMap getBlueMap() {
			return WVWMatch.this.getBlueMap().createImmutableReference();
		}

		@Override
		public IWVWMap getRedMap() {
			return WVWMatch.this.getRedMap().createImmutableReference();
		}

		@Override
		public IWVWMap getGreenMap() {
			return WVWMatch.this.getGreenMap().createImmutableReference();
		}

		@Override
		public IWVWScores getScores() {
			return WVWMatch.this.getScores().createImmutableReference();
		}

		@Override
		public IWVWMatch createImmutableReference() {
			return this;
		}

		@Override
		public EventBus getChannel() {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is only a decorator for " + WVWMatch.class.getSimpleName()
					+ " and has no channel for its own.");
		}

	}

	private static final Logger LOGGER = Logger.getLogger(WVWMatch.class);
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);
	private static final IModelFactory MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IModelFactory.class);

	public static class WVWMatchBuilder implements IWVWMatch.IWVWMatchBuilder {
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
			final IWVWMatch match = new WVWMatch(this.id.get(), this.redWorld.get(), this.greenWorld.get(), this.blueWorld.get(), this.centerMap.get(),
					this.redMap.get(), this.greenMap.get(), this.blueMap.get());
			if (this.fromMatchDTO.isPresent()) {
				checkState(this.fromMatchDTO.get().getDetails().isPresent());
				this.setupOwner(match, match.getCenterMap(), this.fromMatchDTO.get(), this.fromMatchDTO.get().getDetails().get().getCenterMap());
				this.setupOwner(match, match.getBlueMap(), this.fromMatchDTO.get(), this.fromMatchDTO.get().getDetails().get().getBlueMap());
				this.setupOwner(match, match.getRedMap(), this.fromMatchDTO.get(), this.fromMatchDTO.get().getDetails().get().getRedMap());
				this.setupOwner(match, match.getGreenMap(), this.fromMatchDTO.get(), this.fromMatchDTO.get().getDetails().get().getGreenMap());
			}
			match.getScores().update(this.redScore.or(0), this.greenScore.or(0), this.blueScore.or(0));
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
					owner = match.getWorldByDTOOwnerString(objectiveDTO.getOwner()).get();
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
			this.centerMap = Optional.of(WVW_MODEL_FACTORY.newMapBuilder().fromDTO(dto.getDetails().get().getCenterMap()).build());
			checkState(this.centerMap.get().getType().isCenter());
			this.redMap = Optional.of(WVW_MODEL_FACTORY.newMapBuilder().fromDTO(dto.getDetails().get().getRedMap()).build());
			checkState(this.redMap.get().getType().isRed());
			this.greenMap = Optional.of(WVW_MODEL_FACTORY.newMapBuilder().fromDTO(dto.getDetails().get().getGreenMap()).build());
			checkState(this.greenMap.get().getType().isGreen());
			this.blueMap = Optional.of(WVW_MODEL_FACTORY.newMapBuilder().fromDTO(dto.getDetails().get().getBlueMap()).build());
			checkState(this.blueMap.get().getType().isBlue());
			this.redWorld = Optional.of(MODEL_FACTORY.createWorld(dto.getRedWorldId(), dto.getRedWorldName(locale).get().getName()));
			this.greenWorld = Optional.of(MODEL_FACTORY.createWorld(dto.getGreenWorldId(), dto.getGreenWorldName(locale).get().getName()));
			this.blueWorld = Optional.of(MODEL_FACTORY.createWorld(dto.getBlueWorldId(), dto.getBlueWorldName(locale).get().getName()));

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
		this.scores = WVW_MODEL_FACTORY.newMatchScores(this);
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
	public IWorld getRedWorld() {
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
				.add("blueWorld", this.blueWorld).add("centerMap", this.centerMap).add("redMap", this.redMap).add("greenMap", this.greenMap)
				.add("blueMap", this.blueMap).toString();
	}

	@Override
	public Optional<IWorld> getWorldByDTOOwnerString(String dtoOwnerString) {
		checkNotNull(dtoOwnerString);
		switch (dtoOwnerString.toUpperCase()) {
			case IWVWObjectiveDTO.OWNER_RED_STRING:
				return Optional.of(this.redWorld);
			case IWVWObjectiveDTO.OWNER_GREEN_STRING:
				return Optional.of(this.greenWorld);
			case IWVWObjectiveDTO.OWNER_BLUE_STRING:
				return Optional.of(this.blueWorld);
			default:
				LOGGER.error("Invalid dtoOwnerString: " + dtoOwnerString);
				return Optional.absent();
		}
	}

	@Override
	public Set<IWorld> searchWorldsByNamePattern(Pattern searchPattern) {
		checkNotNull(searchPattern);
		checkState(this.redWorld != null);
		checkState(this.greenWorld != null);
		checkState(this.blueWorld != null);
		final Set<IWorld> result = new HashSet<IWorld>();
		if (this.greenWorld.getName().matches(searchPattern.toString())) {
			result.add(this.greenWorld);
		}
		if (this.blueWorld.getName().matches(searchPattern.toString())) {
			result.add(this.blueWorld);
		}
		if (this.redWorld.getName().matches(searchPattern.toString())) {
			result.add(this.redWorld);
		}
		return ImmutableSet.copyOf(result);
	}

	@Override
	public IWVWMatch createImmutableReference() {
		return new WVWImmutableMatchDecorator();
	}
}
