package de.justi.gw2.model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.justi.gw2.api.dto.IWVWMapDTO;
import de.justi.gw2.api.dto.IWVWObjectiveDTO;
import de.justi.gw2.model.AbstractHasChannel;
import de.justi.gw2.model.IHasChannel;
import de.justi.gw2.model.IImmutable;
import de.justi.gw2.model.wvw.IHasWVWLocation;
import de.justi.gw2.model.wvw.IWVWMap;
import de.justi.gw2.model.wvw.IWVWMatch;
import de.justi.gw2.model.wvw.IWVWModelFactory;
import de.justi.gw2.model.wvw.IWVWObjective;
import de.justi.gw2.model.wvw.IWVWScores;
import de.justi.gw2.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.gw2.model.wvw.events.IWVWObjectiveEvent;
import de.justi.gw2.model.wvw.types.IWVWLocationType;
import de.justi.gw2.model.wvw.types.IWVWMapType;
import de.justi.gw2.model.wvw.types.impl.WVWLocationType;
import de.justi.gw2.model.wvw.types.impl.WVWMapType;
import de.justi.gw2.utils.InjectionHelper;

class WVWMap extends AbstractHasChannel implements IWVWMap {

	private static final Logger LOGGER = Logger.getLogger(WVWMap.class);
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);
	
	class WVWImmutableMapDecorator implements IWVWMap, IImmutable{

		@Override
		public EventBus getChannel() {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is only a decorator for " + WVWMap.class.getSimpleName()
					+ " and has no channel for its own.");
		}

		@Override
		public IWVWMapType getType() {
			return WVWMap.this.getType();
		}

		@Override
		public Map<IWVWLocationType, IHasWVWLocation<?>> getMappedByPosition() {
			final Map<IWVWLocationType, IHasWVWLocation<?>> mutableResult = WVWMap.this.getMappedByPosition();
			final Map<IWVWLocationType, IHasWVWLocation<?>> buffer = new HashMap<IWVWLocationType, IHasWVWLocation<?>>();
			for(IWVWLocationType key : WVWMap.this.getMappedByPosition().keySet()) {
				buffer.put(key, mutableResult.get(key).createImmutableReference());
			}
			return ImmutableMap.copyOf(buffer);
		}

		@Override
		public Set<IHasWVWLocation<?>> getEverything() {
			final Set<IHasWVWLocation<?>> mutableResult = WVWMap.this.getEverything();
			final Set<IHasWVWLocation<?>> buffer = new HashSet<IHasWVWLocation<?>>();
			for(IHasWVWLocation<?> element : mutableResult) {
				buffer.add(element.createImmutableReference());
			}
			return ImmutableSet.copyOf(buffer);
		}

		@Override
		public Set<IWVWObjective> getObjectives() {
			final Set<IWVWObjective> mutableResult = WVWMap.this.getObjectives();
			final Set<IWVWObjective> buffer = new HashSet<IWVWObjective>();
			for(IWVWObjective element : mutableResult) {
				buffer.add(element.createImmutableReference());
			}
			return ImmutableSet.copyOf(buffer);
		}

		@Override
		public Optional<IWVWObjective> getByObjectiveId(int id) {
			final Optional<IWVWObjective> buffer = WVWMap.this.getByObjectiveId(id);
			return buffer.isPresent() ? Optional.of(buffer.get().createImmutableReference()) : buffer;
		}

		@Override
		public Optional<IHasWVWLocation<?>> getByLocation(IWVWLocationType location) {
			final Optional<IHasWVWLocation<?>> buffer = WVWMap.this.getByLocation(location);
			return buffer.isPresent() ? Optional.<IHasWVWLocation<?>>of(buffer.get().createImmutableReference()) : buffer;
		}

		@Override
		public IWVWScores getScores() {
			return WVWMap.this.getScores().createImmutableReference();
		}

		@Override
		public IWVWMap createImmutableReference() {
			return this;
		}

		@Override
		public Optional<IWVWMatch> getMatch() {
			final Optional<IWVWMatch> buffer = WVWMap.this.getMatch();
			return buffer.isPresent() ? Optional.<IWVWMatch>of(buffer.get().createImmutableReference()) : buffer;
		}

		@Override
		public void connectWithMatch(IWVWMatch map) {
			throw new UnsupportedOperationException(this.getClass().getSimpleName()+" is immutable.");
		}
		public String toString() {
			return Objects.toStringHelper(this).addValue(WVWMap.this.toString()).toString();
		}
		
	}
	

	public static class WVWMapBuilder implements IWVWMap.IWVWMapBuilder {
		private Map<IWVWLocationType, IHasWVWLocation<?>> contentMappedByLocation = new HashMap<IWVWLocationType, IHasWVWLocation<?>>();
		private Optional<IWVWMapType> type = Optional.absent();
		private Optional<IWVWMatch> match = Optional.absent();
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
			
			if(this.match.isPresent()) {
				map.connectWithMatch(this.match.get());
			}
			
			for (IWVWObjective objective : map.getObjectives()){
				objective.connectWithMap(map);
			}
			
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

		@Override
		public IWVWMapBuilder match(IWVWMatch match) {
			this.match = Optional.fromNullable(match);
			return this;
		}

	}
	
	
	private final IWVWMapType type;
	private final Map<IWVWLocationType, IHasWVWLocation<?>> content;
	private final IWVWScores scores;
	private Optional<IWVWMatch> match = Optional.absent();
	
	private WVWMap(IWVWMapType type, Collection<IHasWVWLocation<?>> contents) {
		checkNotNull(type);
		checkNotNull(contents);
		checkArgument(contents.size() > 0);

		this.type = type;
		final ImmutableMap.Builder<IWVWLocationType, IHasWVWLocation<?>> contentBuilder = ImmutableMap.builder();
		for (IHasWVWLocation<?> content : contents) {
			contentBuilder.put(content.getLocation(), content);
		}
		this.content = contentBuilder.build();
		this.scores = WVW_MODEL_FACTORY.newMapScores(this);
		
		// register as listener to scores
		this.scores.getChannel().register(this);
		
		// register as listener to content with channels
		for(IHasChannel contentWithChannel : Iterables.filter(this.content.values(), IHasChannel.class)) {
			contentWithChannel.getChannel().register(this);
		}
	}
	
	@Subscribe
	public void onWVWObjectiveEvent(IWVWObjectiveEvent event) {
		if(LOGGER.isTraceEnabled()) {
			LOGGER.trace(this.getClass().getSimpleName()+" is going to forward "+event);
		}
		this.getChannel().post(event);
	}
	
	@Subscribe
	public void onWVWMapScoreChangeEvent(IWVWMapScoresChangedEvent event) {
		if(LOGGER.isTraceEnabled()) {
			LOGGER.trace(this.getClass().getSimpleName()+" is going to forward "+event);
		}
		this.getChannel().post(event);
	}

	public IWVWMapType getType() {
		return this.type;
	}

	public Map<IWVWLocationType, IHasWVWLocation<?>> getMappedByPosition() {
		return this.content;
	}

	public Set<IHasWVWLocation<?>> getEverything() {
		return ImmutableSet.copyOf(this.content.values());
	}

	public Set<IWVWObjective> getObjectives() {
		return ImmutableSet.copyOf(Iterables.filter(this.content.values(), IWVWObjective.class));
	}

	public Optional<IHasWVWLocation<?>> getByLocation(IWVWLocationType location) {
		if (this.content.containsKey(location)) {
			return Optional.<IHasWVWLocation<?>>fromNullable(this.content.get(location));
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
			final Optional<IHasWVWLocation<?>> content = this.getByLocation(location.get());
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

	@Override
	public IWVWMap createImmutableReference() {
		return new WVWImmutableMapDecorator();
	}

	@Override
	public Optional<IWVWMatch> getMatch() {
		return this.match;
	}

	@Override
	public void connectWithMatch(IWVWMatch match) {
		checkNotNull(match);
		checkState(!this.match.isPresent(),"Connect with map can only be called once.");
		this.match = Optional.of(match);
	}

}
