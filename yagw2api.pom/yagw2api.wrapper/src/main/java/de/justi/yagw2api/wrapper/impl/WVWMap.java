package de.justi.yagw2api.wrapper.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RecursiveAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.justi.yagw2api.arenanet.IWVWMapDTO;
import de.justi.yagw2api.arenanet.IWVWObjectiveDTO;
import de.justi.yagw2api.wrapper.IHasWVWLocation;
import de.justi.yagw2api.wrapper.IWVWLocationType;
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWMapType;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWModelFactory;
import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWVWObjectiveEvent;
import de.justi.yagw2api.wrapper.IWVWScores;
import de.justi.yagw2api.wrapper.IWorld;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagwapi.common.AbstractHasChannel;
import de.justi.yagwapi.common.IHasChannel;
import de.justi.yagwapi.common.IUnmodifiable;

final class WVWMap extends AbstractHasChannel implements IWVWMap {

	private static final Logger LOGGER = LoggerFactory.getLogger(WVWMap.class);
	private static final IWVWModelFactory WVW_MODEL_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWModelFactory();

	class UnmodifiableWVWMap implements IWVWMap, IUnmodifiable {

		@Override
		public EventBus getChannel() {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName()
					+ " and therefore can not be modified.");
		}

		@Override
		public IWVWMapType getType() {
			return WVWMap.this.getType();
		}

		@Override
		public Map<IWVWLocationType, IHasWVWLocation<?>> getMappedByPosition() {
			final Map<IWVWLocationType, IHasWVWLocation<?>> mutableResult = WVWMap.this.getMappedByPosition();
			final Map<IWVWLocationType, IHasWVWLocation<?>> buffer = new HashMap<IWVWLocationType, IHasWVWLocation<?>>();
			for (IWVWLocationType key : WVWMap.this.getMappedByPosition().keySet()) {
				buffer.put(key, mutableResult.get(key).createUnmodifiableReference());
			}
			return Collections.unmodifiableMap(buffer);
		}

		@Override
		public Set<IHasWVWLocation<?>> getEverything() {
			final Set<IHasWVWLocation<?>> mutableResult = WVWMap.this.getEverything();
			final Set<IHasWVWLocation<?>> buffer = new HashSet<IHasWVWLocation<?>>();
			for (IHasWVWLocation<?> element : mutableResult) {
				buffer.add(element.createUnmodifiableReference());
			}
			return Collections.unmodifiableSet(buffer);
		}

		@Override
		public Set<IWVWObjective> getObjectives() {
			final Set<IWVWObjective> mutableResult = WVWMap.this.getObjectives();
			final Set<IWVWObjective> buffer = new HashSet<IWVWObjective>();
			for (IWVWObjective element : mutableResult) {
				buffer.add(element.createUnmodifiableReference());
			}
			return Collections.unmodifiableSet(buffer);
		}

		@Override
		public Optional<IWVWObjective> getByObjectiveId(final int id) {
			final Optional<IWVWObjective> buffer = WVWMap.this.getByObjectiveId(id);
			return buffer.isPresent() ? Optional.of(buffer.get().createUnmodifiableReference()) : buffer;
		}

		@Override
		public Optional<IHasWVWLocation<?>> getByLocation(final IWVWLocationType location) {
			final Optional<IHasWVWLocation<?>> buffer = WVWMap.this.getByLocation(location);
			return buffer.isPresent() ? Optional.<IHasWVWLocation<?>> of(buffer.get().createUnmodifiableReference()) : buffer;
		}

		@Override
		public IWVWScores getScores() {
			return WVWMap.this.getScores().createUnmodifiableReference();
		}

		@Override
		public IWVWMap createUnmodifiableReference() {
			return this;
		}

		@Override
		public Optional<IWVWMatch> getMatch() {
			final Optional<IWVWMatch> buffer = WVWMap.this.getMatch();
			return buffer.isPresent() ? Optional.<IWVWMatch> of(buffer.get().createUnmodifiableReference()) : buffer;
		}

		@Override
		public void connectWithMatch(final IWVWMatch map) {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName()
					+ " and therefore can not be modified.");
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).addValue(WVWMap.this.toString()).toString();
		}

		@Override
		public int calculateGreenTick() {
			return WVWMap.this.calculateGreenTick();
		}

		@Override
		public int calculateBlueTick() {
			return WVWMap.this.calculateBlueTick();
		}

		@Override
		public int calculateRedTick() {
			return WVWMap.this.calculateRedTick();
		}

	}

	private static final class BuildObjectivesFromDTOsAction extends RecursiveAction {
		private static final long serialVersionUID = -2652032220819553149L;

		private final List<IWVWObjective> results;
		private final List<IWVWObjectiveDTO> objectiveDTOs;

		public BuildObjectivesFromDTOsAction(final IWVWObjectiveDTO[] objectiveDTOs) {
			this(ImmutableList.copyOf(checkNotNull(objectiveDTOs)), new ArrayList<IWVWObjective>());
		}

		private BuildObjectivesFromDTOsAction(final List<IWVWObjectiveDTO> dtos, final List<IWVWObjective> results) {
			checkNotNull(dtos);
			this.results = checkNotNull(results, "Missing objective list.");
			this.objectiveDTOs = dtos;
		}

		@SuppressWarnings("unchecked")
		private List<IWVWObjectiveDTO> getFirstHalf() {
			if (this.objectiveDTOs.isEmpty()) {
				return Collections.EMPTY_LIST;
			} else {
				return this.objectiveDTOs.subList(0, this.objectiveDTOs.size() / 2);
			}
		}

		@SuppressWarnings("unchecked")
		private List<IWVWObjectiveDTO> getSecondHalf() {
			if (this.objectiveDTOs.size() < 2) {
				return Collections.EMPTY_LIST;
			} else {
				return this.objectiveDTOs.subList(this.objectiveDTOs.size() / 2, this.objectiveDTOs.size());
			}
		}

		@Override
		protected void compute() {
			if (this.objectiveDTOs.size() <= 1) {
				if (this.objectiveDTOs.size() == 1) {
					this.results.add(WVW_MODEL_FACTORY.newObjectiveBuilder().fromDTO(this.objectiveDTOs.get(0)).build());
				}
			} else {
				invokeAll(new BuildObjectivesFromDTOsAction(this.getFirstHalf(), this.results), new BuildObjectivesFromDTOsAction(this.getSecondHalf(), this.results));
			}
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("dtoCount", this.objectiveDTOs.size()).add("presentResultsCount", this.results.size()).toString();
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

			if (this.match.isPresent()) {
				map.connectWithMatch(this.match.get());
			}

			for (IWVWObjective objective : map.getObjectives()) {
				objective.connectWithMap(map);
			}

			return map;
		}

		@Override
		public IWVWMap.IWVWMapBuilder type(final IWVWMapType type) {
			this.type = Optional.fromNullable(type);
			return this;
		}

		@Override
		public IWVWMap.IWVWMapBuilder objective(final IWVWObjective objective) {
			checkNotNull(objective);
			checkNotNull(objective.getLocation());
			checkState(!this.contentMappedByLocation.containsKey(objective.getLocation()));
			this.contentMappedByLocation.put(objective.getLocation(), objective);
			return this;
		}

		@Override
		public IWVWMap.IWVWMapBuilder fromDTO(final IWVWMapDTO dto) {
			checkNotNull(dto);
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Going to build from " + IWVWMapDTO.class.getSimpleName() + " where dto=" + dto);
			}

			final BuildObjectivesFromDTOsAction initObjectivesAction = new BuildObjectivesFromDTOsAction(dto.getObjectives());
			YAGW2APIWrapper.INSTANCE.getForkJoinPool().invoke(initObjectivesAction);

			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Done with execution of " + initObjectivesAction.toString() + " for " + dto);
			}

			this.type(WVWMapType.fromDTOString(dto.getType()));
			return this.blueScore(dto.getBlueScore()).redScore(dto.getRedScore()).greenScore(dto.getGreenScore());
		}

		@Override
		public IWVWMap.IWVWMapBuilder redScore(final int score) {
			checkArgument(score > 0);
			this.redScore = Optional.of(score);
			return this;
		}

		@Override
		public IWVWMap.IWVWMapBuilder blueScore(final int score) {
			checkArgument(score > 0);
			this.blueScore = Optional.of(score);
			return this;
		}

		@Override
		public IWVWMap.IWVWMapBuilder greenScore(final int score) {
			checkArgument(score > 0);
			this.greenScore = Optional.of(score);
			return this;
		}

		@Override
		public IWVWMapBuilder match(final IWVWMatch match) {
			this.match = Optional.fromNullable(match);
			return this;
		}

	}

	private final IWVWMapType type;
	private final Map<IWVWLocationType, IHasWVWLocation<?>> content;
	private final IWVWScores scores;
	private Optional<IWVWMatch> match = Optional.absent();

	private WVWMap(final IWVWMapType type, final Collection<IHasWVWLocation<?>> contents) {
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
		for (IHasChannel contentWithChannel : Iterables.filter(this.content.values(), IHasChannel.class)) {
			contentWithChannel.getChannel().register(this);
		}
	}

	@Subscribe
	public void onWVWObjectiveEvent(final IWVWObjectiveEvent event) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace(this.getClass().getSimpleName() + " is going to forward " + event);
		}
		this.getChannel().post(event);
	}

	@Subscribe
	public void onWVWMapScoreChangeEvent(final IWVWMapScoresChangedEvent event) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace(this.getClass().getSimpleName() + " is going to forward " + event);
		}
		this.getChannel().post(event);
	}

	@Override
	public IWVWMapType getType() {
		return this.type;
	}

	@Override
	public Map<IWVWLocationType, IHasWVWLocation<?>> getMappedByPosition() {
		return this.content;
	}

	@Override
	public Set<IHasWVWLocation<?>> getEverything() {
		return ImmutableSet.copyOf(this.content.values());
	}

	@Override
	public Set<IWVWObjective> getObjectives() {
		return ImmutableSet.copyOf(Iterables.filter(this.content.values(), IWVWObjective.class));
	}

	@Override
	public Optional<IHasWVWLocation<?>> getByLocation(final IWVWLocationType location) {
		if (this.content.containsKey(location)) {
			return Optional.<IHasWVWLocation<?>> fromNullable(this.content.get(location));
		} else {
			return Optional.absent();
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("type", this.type).add("contentCount", this.content.size()).add("scored", this.scores).toString();
	}

	@Override
	public IWVWScores getScores() {
		return this.scores;
	}

	@Override
	public Optional<IWVWObjective> getByObjectiveId(final int id) {
		checkArgument(id > 0);
		final Optional<IWVWLocationType> location = WVWLocationType.forObjectiveId(id);
		if (location.isPresent()) {
			final Optional<IHasWVWLocation<?>> content = this.getByLocation(location.get());
			if (content.isPresent()) {
				checkState(content.get() instanceof IWVWObjective);
				return Optional.of((IWVWObjective) content.get());
			} else {
				return Optional.absent();
			}
		} else {
			return Optional.absent();
		}
	}

	@Override
	public IWVWMap createUnmodifiableReference() {
		return new UnmodifiableWVWMap();
	}

	@Override
	public Optional<IWVWMatch> getMatch() {
		return this.match;
	}

	@Override
	public void connectWithMatch(final IWVWMatch match) {
		checkNotNull(match);
		checkState(!this.match.isPresent(), "Connect with map can only be called once.");
		this.match = Optional.of(match);
	}

	private int calculateTickOfWorld(final IWorld world) {
		int tick = 0;
		for (IWVWObjective objective : this.getObjectives()) {
			if (world.equals(objective.getOwner().orNull())) {
				tick += objective.getType().getPoints();
			}
		}
		return tick;
	}

	@Override
	public int calculateGreenTick() {
		int tick = 0;
		final Optional<IWVWMatch> match = this.getMatch();
		if (match.isPresent()) {
			tick = this.calculateTickOfWorld(match.get().getGreenWorld());
		}
		return tick;
	}

	@Override
	public int calculateBlueTick() {
		int tick = 0;
		final Optional<IWVWMatch> match = this.getMatch();
		if (match.isPresent()) {
			tick = this.calculateTickOfWorld(match.get().getBlueWorld());
		}
		return tick;
	}

	@Override
	public int calculateRedTick() {
		int tick = 0;
		final Optional<IWVWMatch> match = this.getMatch();
		if (match.isPresent()) {
			tick = this.calculateTickOfWorld(match.get().getRedWorld());
		}
		return tick;
	}

}
