package de.justi.yagw2api.core.wrapper.model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.justi.yagw2api.core.YAGW2APICore;
import de.justi.yagw2api.core.arenanet.dto.IWVWMapDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDetailsDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWObjectiveDTO;
import de.justi.yagw2api.core.wrapper.model.AbstractHasChannel;
import de.justi.yagw2api.core.wrapper.model.IEvent;
import de.justi.yagw2api.core.wrapper.model.IModelFactory;
import de.justi.yagw2api.core.wrapper.model.IUnmodifiable;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWModelFactory;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWScores;

class WVWMatch extends AbstractHasChannel implements IWVWMatch {

	final class UnmodifiableWVWMatch implements IWVWMatch, IUnmodifiable {

		@Override
		public Set<IWorld> searchWorldsByNamePattern(Pattern searchPattern) {
			final Collection<IWorld> result = new ArrayList<IWorld>();
			for (IWorld world : WVWMatch.this.searchWorldsByNamePattern(searchPattern)) {
				result.add(world.createUnmodifiableReference());
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
				buffer.add(world.createUnmodifiableReference());
			}
			return buffer.toArray(new IWorld[0]);
		}

		@Override
		public IWorld getRedWorld() {
			return WVWMatch.this.getRedWorld().createUnmodifiableReference();
		}

		@Override
		public IWorld getGreenWorld() {
			return WVWMatch.this.getGreenWorld().createUnmodifiableReference();
		}

		@Override
		public IWorld getBlueWorld() {
			return WVWMatch.this.getBlueWorld().createUnmodifiableReference();
		}

		@Override
		public Optional<IWorld> getWorldByDTOOwnerString(String dtoOwnerString) {
			final Optional<IWorld> buffer = WVWMatch.this.getWorldByDTOOwnerString(dtoOwnerString);
			return buffer.isPresent() ? Optional.of(buffer.get().createUnmodifiableReference()) : buffer;
		}

		@Override
		public IWVWMap getCenterMap() {
			return WVWMatch.this.getCenterMap().createUnmodifiableReference();
		}

		@Override
		public IWVWMap getBlueMap() {
			return WVWMatch.this.getBlueMap().createUnmodifiableReference();
		}

		@Override
		public IWVWMap getRedMap() {
			return WVWMatch.this.getRedMap().createUnmodifiableReference();
		}

		@Override
		public IWVWMap getGreenMap() {
			return WVWMatch.this.getGreenMap().createUnmodifiableReference();
		}

		@Override
		public IWVWScores getScores() {
			return WVWMatch.this.getScores().createUnmodifiableReference();
		}

		@Override
		public IWVWMatch createUnmodifiableReference() {
			return this;
		}

		@Override
		public EventBus getChannel() {
			throw new UnsupportedOperationException(this.getClass().getSimpleName()+" is instance of "+IUnmodifiable.class.getSimpleName()+" and therefore can not be modified.");
		}

		public String toString() {
			return Objects.toStringHelper(this).addValue(WVWMatch.this.toString()).toString();
		}
		
		@Override
		public int hashCode() {		
			return WVWMatch.this.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return WVWMatch.this.equals(obj);
		}
	}

	private static final Logger LOGGER = Logger.getLogger(WVWMatch.class);
	private static final IWVWModelFactory WVW_MODEL_FACTORY = YAGW2APICore.getInjector().getInstance(IWVWModelFactory.class);
	private static final IModelFactory MODEL_FACTORY = YAGW2APICore.getInjector().getInstance(IModelFactory.class);

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
			checkState(this.id.isPresent());
			checkState(this.centerMap.isPresent());
			checkState(this.redMap.isPresent());
			checkState(this.greenMap.isPresent());
			checkState(this.blueMap.isPresent());
			checkState(this.redWorld.isPresent());
			checkState(this.greenWorld.isPresent());
			checkState(this.blueWorld.isPresent());
			
			
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

			match.getRedMap().connectWithMatch(match);
			match.getGreenMap().connectWithMatch(match);
			match.getBlueMap().connectWithMatch(match);
			match.getCenterMap().connectWithMatch(match);

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
			
			this.redWorld = MODEL_FACTORY.getWorld(dto.getRedWorldId());
			if(!this.redWorld.isPresent()) {
				checkArgument(dto.getRedWorldName(Locale.getDefault()).isPresent());
				this.redWorld=Optional.of(MODEL_FACTORY.newWorldBuilder().fromDTO(dto.getRedWorldName(Locale.getDefault()).get()).build());
			}
			this.greenWorld = MODEL_FACTORY.getWorld(dto.getGreenWorldId());
			if(!this.greenWorld.isPresent()) {
				checkArgument(dto.getGreenWorldName(Locale.getDefault()).isPresent());
				this.greenWorld=Optional.of(MODEL_FACTORY.newWorldBuilder().fromDTO(dto.getGreenWorldName(Locale.getDefault()).get()).build());
			}
			this.blueWorld = MODEL_FACTORY.getWorld(dto.getBlueWorldId());
			if(!this.blueWorld.isPresent()) {
				checkArgument(dto.getBlueWorldName(Locale.getDefault()).isPresent());
				this.blueWorld=Optional.of(MODEL_FACTORY.newWorldBuilder().fromDTO(dto.getBlueWorldName(Locale.getDefault()).get()).build());
			}

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

		// register as listener
		this.centerMap.getChannel().register(this);
		this.redMap.getChannel().register(this);
		this.greenMap.getChannel().register(this);
		this.blueMap.getChannel().register(this);

		// register as listener to scores
		this.scores.getChannel().register(this);
	}

	@Subscribe
	public void onEvent(IEvent event) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace(this.getClass().getSimpleName() + " is going to forward " + event);
		}
		this.getChannel().post(event);
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
		if (this.greenWorld.getName().isPresent() && this.greenWorld.getName().get().matches(searchPattern.toString())) {
			result.add(this.greenWorld);
		}
		if (this.blueWorld.getName().isPresent() && this.blueWorld.getName().get().matches(searchPattern.toString())) {
			result.add(this.blueWorld);
		}
		if (this.redWorld.getName().isPresent() && this.redWorld.getName().get().matches(searchPattern.toString())) {
			result.add(this.redWorld);
		}
		return ImmutableSet.copyOf(result);
	}

	@Override
	public IWVWMatch createUnmodifiableReference() {
		return new UnmodifiableWVWMatch();
	}
	@Override
	public int hashCode() {		
		return Objects.hashCode(this.getClass().getName(),this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || ! (obj instanceof IWVWMatch)) {
			return false;
		}else{
			final IWVWMatch match = (IWVWMatch)obj;
			return Objects.equal(this.id, match.getId());
		}
	}
}
