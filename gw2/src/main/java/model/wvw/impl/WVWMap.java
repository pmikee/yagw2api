package model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import model.AbstractHasChannel;
import model.wvw.IHasWVWLocation;
import model.wvw.IWVWMap;
import model.wvw.IWVWMapType;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWScores;
import model.wvw.types.IWVWLocationType;
import model.wvw.types.IWVWObjective;
import model.wvw.types.impl.WVWLocationType;
import model.wvw.types.impl.WVWMapType;
import utils.InjectionHelper;
import api.dto.IWVWMapDTO;
import api.dto.IWVWObjectiveDTO;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

class WVWMap extends AbstractHasChannel implements IWVWMap {
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);

	public static class WVWMapBuilder implements IWVWMap.IWVWMapBuilder {
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
						this.contentMappedByLocation.put(location, WVW_MODEL_FACTORY.newObjectiveBuilder().location(location).build());
					} else {
						// TODO build non objective map content for locations
					}
				}
			}
			final IWVWMap map = new WVWMap(this.type.get(), this.contentMappedByLocation.values());
			map.getScores().update(this.redScore.or(0), this.greenScore.or(0), this.blueScore.or(0));
			return map;
		}

		@Override
		public IWVWMap.IWVWMapBuilder type(IWVWMapType type) {
			this.type = Optional.fromNullable(type);
			return this;
		}

		@Override
		public IWVWMap.IWVWMapBuilder objective(IWVWObjective objective) {
			checkNotNull(objective);
			checkNotNull(objective.getLocation());
			checkState(!this.contentMappedByLocation.containsKey(objective.getLocation()));
			this.contentMappedByLocation.put(objective.getLocation(), objective);
			return this;
		}

		@Override
		public IWVWMap.IWVWMapBuilder fromDTO(IWVWMapDTO dto) {
			checkNotNull(dto);
			for (IWVWObjectiveDTO objectiveDTO : dto.getObjectives()) {
				this.objective(WVW_MODEL_FACTORY.newObjectiveBuilder().fromDTO(objectiveDTO).build());
			}
			this.type(WVWMapType.fromDTOString(dto.getType()));		
			return this.blueScore(dto.getBlueScore()).redScore(dto.getRedScore()).greenScore(dto.getGreenScore());
		}

		@Override
		public IWVWMap.IWVWMapBuilder redScore(int score) {
			checkArgument(score > 0);
			this.redScore = Optional.of(score);
			return this;
		}

		@Override
		public IWVWMap.IWVWMapBuilder blueScore(int score) {
			checkArgument(score > 0);
			this.blueScore = Optional.of(score);
			return this;
		}

		@Override
		public IWVWMap.IWVWMapBuilder greenScore(int score) {
			checkArgument(score > 0);
			this.greenScore = Optional.of(score);
			return this;
		}

	}
	
	
	private final IWVWMapType type;
	private final Map<IWVWLocationType, IHasWVWLocation> content;
	private final IWVWScores scores;

	private WVWMap(IWVWMapType type, Collection<IHasWVWLocation> contents) {
		checkNotNull(type);
		checkNotNull(contents);
		checkArgument(contents.size() > 0);

		this.type = type;
		final ImmutableMap.Builder<IWVWLocationType, IHasWVWLocation> contentBuilder = ImmutableMap.builder();
		for (IHasWVWLocation content : contents) {
			contentBuilder.put(content.getLocation(), content);
		}
		this.content = contentBuilder.build();
		this.scores = WVW_MODEL_FACTORY.newMapScores(this);
	}

	public IWVWMapType getType() {
		return this.type;
	}

	public Map<IWVWLocationType, IHasWVWLocation> getMappedByPosition() {
		return this.content;
	}

	public Set<IHasWVWLocation> getEverything() {
		return ImmutableSet.copyOf(this.content.values());
	}

	public Set<IWVWObjective> getObjectives() {
		return ImmutableSet.copyOf(Iterables.filter(this.content.values(), IWVWObjective.class));
	}

	public Optional<IHasWVWLocation> getByLocation(IWVWLocationType location) {
		if (this.content.containsKey(location)) {
			return Optional.fromNullable(this.content.get(location));
		} else {
			return Optional.absent();
		}
	}

	public String toString() {
		return Objects.toStringHelper(this).add("type", this.type).add("content", this.content).add("scored", this.scores).toString();
	}

	@Override
	public IWVWScores getScores() {
		return this.scores;
	}

	@Override
	public Optional<IWVWObjective> getByObjectiveId(int id) {
		checkArgument(id >0);
		final Optional<IWVWLocationType> location = WVWLocationType.forObjectiveId(id);
		if(location.isPresent()) {
			final Optional<IHasWVWLocation> content = this.getByLocation(location.get());
			if(content.isPresent()) {
				checkState(content.get() instanceof IWVWObjective);
				return Optional.of((IWVWObjective)content.get());
			}else {
				return Optional.absent();	
			}
		}else {
			return Optional.absent();
		}
	}

}
