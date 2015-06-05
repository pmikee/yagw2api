package de.justi.yagw2api.wrapper.wvw.domain.impl;

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

import de.justi.yagw2api.arenanet.dto.wvw.WVWMapDTO;
import de.justi.yagw2api.arenanet.dto.wvw.WVWObjectiveDTO;
import de.justi.yagw2api.common.event.AbstractHasChannel;
import de.justi.yagw2api.common.event.HasChannel;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.world.domain.World;
import de.justi.yagw2api.wrapper.wvw.domain.HasWVWLocation;
import de.justi.yagw2api.wrapper.wvw.domain.WVWDomainFactory;
import de.justi.yagw2api.wrapper.wvw.domain.WVWLocationType;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMap;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMapType;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMatch;
import de.justi.yagw2api.wrapper.wvw.domain.WVWObjective;
import de.justi.yagw2api.wrapper.wvw.domain.WVWScores;
import de.justi.yagw2api.wrapper.wvw.event.WVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveEvent;

final class DefaultWVWMap extends AbstractHasChannel implements WVWMap {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWVWMap.class);
	private static final WVWDomainFactory WVW_MODEL_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWDomainFactory();

	class UnmodifiableWVWMap implements WVWMap {

		@Override
		public EventBus getChannel() {
			throw new UnsupportedOperationException("unmodifiable");
		}

		@Override
		public WVWMapType getType() {
			return DefaultWVWMap.this.getType();
		}

		@Override
		public Map<WVWLocationType, HasWVWLocation<?>> getMappedByPosition() {
			final Map<WVWLocationType, HasWVWLocation<?>> mutableResult = DefaultWVWMap.this.getMappedByPosition();
			final Map<WVWLocationType, HasWVWLocation<?>> buffer = new HashMap<WVWLocationType, HasWVWLocation<?>>();
			for (WVWLocationType key : DefaultWVWMap.this.getMappedByPosition().keySet()) {
				buffer.put(key, mutableResult.get(key).createUnmodifiableReference());
			}
			return Collections.unmodifiableMap(buffer);
		}

		@Override
		public Set<HasWVWLocation<?>> getEverything() {
			final Set<HasWVWLocation<?>> mutableResult = DefaultWVWMap.this.getEverything();
			final Set<HasWVWLocation<?>> buffer = new HashSet<HasWVWLocation<?>>();
			for (HasWVWLocation<?> element : mutableResult) {
				buffer.add(element.createUnmodifiableReference());
			}
			return Collections.unmodifiableSet(buffer);
		}

		@Override
		public Set<WVWObjective> getObjectives() {
			final Set<WVWObjective> mutableResult = DefaultWVWMap.this.getObjectives();
			final Set<WVWObjective> buffer = new HashSet<WVWObjective>();
			for (WVWObjective element : mutableResult) {
				buffer.add(element.createUnmodifiableReference());
			}
			return Collections.unmodifiableSet(buffer);
		}

		@Override
		public Optional<WVWObjective> getByObjectiveId(final int id) {
			final Optional<WVWObjective> buffer = DefaultWVWMap.this.getByObjectiveId(id);
			return buffer.isPresent() ? Optional.of(buffer.get().createUnmodifiableReference()) : buffer;
		}

		@Override
		public Optional<HasWVWLocation<?>> getByLocation(final WVWLocationType location) {
			final Optional<HasWVWLocation<?>> buffer = DefaultWVWMap.this.getByLocation(location);
			return buffer.isPresent() ? Optional.<HasWVWLocation<?>> of(buffer.get().createUnmodifiableReference()) : buffer;
		}

		@Override
		public WVWScores getScores() {
			return DefaultWVWMap.this.getScores().createUnmodifiableReference();
		}

		@Override
		public WVWMap createUnmodifiableReference() {
			return this;
		}

		@Override
		public Optional<WVWMatch> getMatch() {
			final Optional<WVWMatch> buffer = DefaultWVWMap.this.getMatch();
			return buffer.isPresent() ? Optional.<WVWMatch> of(buffer.get().createUnmodifiableReference()) : buffer;
		}

		@Override
		public void connectWithMatch(final WVWMatch map) {
			throw new UnsupportedOperationException("unmodifiable");
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).addValue(DefaultWVWMap.this.toString()).toString();
		}

		@Override
		public int calculateGreenTick() {
			return DefaultWVWMap.this.calculateGreenTick();
		}

		@Override
		public int calculateBlueTick() {
			return DefaultWVWMap.this.calculateBlueTick();
		}

		@Override
		public int calculateRedTick() {
			return DefaultWVWMap.this.calculateRedTick();
		}

	}

	private static final class BuildObjectivesFromDTOsAction extends RecursiveAction {
		private static final long serialVersionUID = -2652032220819553149L;

		private final List<WVWObjective> results;
		private final List<WVWObjectiveDTO> objectiveDTOs;

		public BuildObjectivesFromDTOsAction(final WVWObjectiveDTO[] objectiveDTOs) {
			this(ImmutableList.copyOf(checkNotNull(objectiveDTOs)), new ArrayList<WVWObjective>());
		}

		private BuildObjectivesFromDTOsAction(final List<WVWObjectiveDTO> dtos, final List<WVWObjective> results) {
			checkNotNull(dtos);
			this.results = checkNotNull(results, "Missing objective list.");
			this.objectiveDTOs = dtos;
		}

		@SuppressWarnings("unchecked")
		private List<WVWObjectiveDTO> getFirstHalf() {
			if (this.objectiveDTOs.isEmpty()) {
				return Collections.EMPTY_LIST;
			} else {
				return this.objectiveDTOs.subList(0, this.objectiveDTOs.size() / 2);
			}
		}

		@SuppressWarnings("unchecked")
		private List<WVWObjectiveDTO> getSecondHalf() {
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

	public static class DefaultWVWMapBuilder implements WVWMap.WVWMapBuilder {
		private Map<WVWLocationType, HasWVWLocation<?>> contentMappedByLocation = new HashMap<WVWLocationType, HasWVWLocation<?>>();
		private Optional<WVWMapType> type = Optional.absent();
		private Optional<WVWMatch> match = Optional.absent();
		private Optional<Integer> redScore = Optional.absent();
		private Optional<Integer> blueScore = Optional.absent();
		private Optional<Integer> greenScore = Optional.absent();

		@Override
		public WVWMap build() {
			checkState(DefaultWVWLocationType.forMapTyp(this.type.get()).isPresent());
			for (WVWLocationType location : DefaultWVWLocationType.forMapTyp(this.type.get()).get()) {
				if (!this.contentMappedByLocation.containsKey(location)) {
					if (location.isObjectiveLocation()) {
						this.contentMappedByLocation.put(location, WVW_MODEL_FACTORY.newObjectiveBuilder().location(location).build());
					} else {
						// TODO build non objective map content for locations
					}
				}
			}
			final WVWMap map = new DefaultWVWMap(this.type.get(), this.contentMappedByLocation.values());
			map.getScores().update(this.redScore.or(0), this.greenScore.or(0), this.blueScore.or(0));

			if (this.match.isPresent()) {
				map.connectWithMatch(this.match.get());
			}

			for (WVWObjective objective : map.getObjectives()) {
				objective.connectWithMap(map);
			}

			return map;
		}

		@Override
		public WVWMap.WVWMapBuilder type(final WVWMapType type) {
			this.type = Optional.fromNullable(type);
			return this;
		}

		@Override
		public WVWMap.WVWMapBuilder objective(final WVWObjective objective) {
			checkNotNull(objective);
			checkNotNull(objective.getLocation());
			checkState(!this.contentMappedByLocation.containsKey(objective.getLocation()));
			this.contentMappedByLocation.put(objective.getLocation(), objective);
			return this;
		}

		@Override
		public WVWMap.WVWMapBuilder fromDTO(final WVWMapDTO dto) {
			checkNotNull(dto);
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Going to build from " + WVWMapDTO.class.getSimpleName() + " where dto=" + dto);
			}

			final BuildObjectivesFromDTOsAction initObjectivesAction = new BuildObjectivesFromDTOsAction(dto.getObjectives());
			YAGW2APIWrapper.INSTANCE.getForkJoinPool().invoke(initObjectivesAction);

			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Done with execution of " + initObjectivesAction.toString() + " for " + dto);
			}

			this.type(DefaultWVWMapType.fromDTOString(dto.getType()));
			return this.blueScore(dto.getBlueScore()).redScore(dto.getRedScore()).greenScore(dto.getGreenScore());
		}

		@Override
		public WVWMap.WVWMapBuilder redScore(final int score) {
			checkArgument(score > 0);
			this.redScore = Optional.of(score);
			return this;
		}

		@Override
		public WVWMap.WVWMapBuilder blueScore(final int score) {
			checkArgument(score > 0);
			this.blueScore = Optional.of(score);
			return this;
		}

		@Override
		public WVWMap.WVWMapBuilder greenScore(final int score) {
			checkArgument(score > 0);
			this.greenScore = Optional.of(score);
			return this;
		}

		@Override
		public WVWMapBuilder match(final WVWMatch match) {
			this.match = Optional.fromNullable(match);
			return this;
		}

	}

	private final WVWMapType type;
	private final Map<WVWLocationType, HasWVWLocation<?>> content;
	private final WVWScores scores;
	private Optional<WVWMatch> match = Optional.absent();

	private DefaultWVWMap(final WVWMapType type, final Collection<HasWVWLocation<?>> contents) {
		checkNotNull(type);
		checkNotNull(contents);
		checkArgument(contents.size() > 0);

		this.type = type;
		final ImmutableMap.Builder<WVWLocationType, HasWVWLocation<?>> contentBuilder = ImmutableMap.builder();
		for (HasWVWLocation<?> content : contents) {
			contentBuilder.put(content.getLocation(), content);
		}
		this.content = contentBuilder.build();
		this.scores = WVW_MODEL_FACTORY.newMapScores(this);

		// register as listener to scores
		this.scores.getChannel().register(this);

		// register as listener to content with channels
		for (HasChannel contentWithChannel : Iterables.filter(this.content.values(), HasChannel.class)) {
			contentWithChannel.getChannel().register(this);
		}
	}

	@Subscribe
	public void onWVWObjectiveEvent(final WVWObjectiveEvent event) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace(this.getClass().getSimpleName() + " is going to forward " + event);
		}
		this.getChannel().post(event);
	}

	@Subscribe
	public void onWVWMapScoreChangeEvent(final WVWMapScoresChangedEvent event) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace(this.getClass().getSimpleName() + " is going to forward " + event);
		}
		this.getChannel().post(event);
	}

	@Override
	public WVWMapType getType() {
		return this.type;
	}

	@Override
	public Map<WVWLocationType, HasWVWLocation<?>> getMappedByPosition() {
		return this.content;
	}

	@Override
	public Set<HasWVWLocation<?>> getEverything() {
		return ImmutableSet.copyOf(this.content.values());
	}

	@Override
	public Set<WVWObjective> getObjectives() {
		return ImmutableSet.copyOf(Iterables.filter(this.content.values(), WVWObjective.class));
	}

	@Override
	public Optional<HasWVWLocation<?>> getByLocation(final WVWLocationType location) {
		if (this.content.containsKey(location)) {
			return Optional.<HasWVWLocation<?>> fromNullable(this.content.get(location));
		} else {
			return Optional.absent();
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("type", this.type).add("contentCount", this.content.size()).add("scored", this.scores).toString();
	}

	@Override
	public WVWScores getScores() {
		return this.scores;
	}

	@Override
	public Optional<WVWObjective> getByObjectiveId(final int id) {
		checkArgument(id > 0);
		final Optional<WVWLocationType> location = DefaultWVWLocationType.forObjectiveId(id);
		if (location.isPresent()) {
			final Optional<HasWVWLocation<?>> content = this.getByLocation(location.get());
			if (content.isPresent()) {
				checkState(content.get() instanceof WVWObjective);
				return Optional.of((WVWObjective) content.get());
			} else {
				return Optional.absent();
			}
		} else {
			return Optional.absent();
		}
	}

	@Override
	public WVWMap createUnmodifiableReference() {
		return new UnmodifiableWVWMap();
	}

	@Override
	public Optional<WVWMatch> getMatch() {
		return this.match;
	}

	@Override
	public void connectWithMatch(final WVWMatch match) {
		checkNotNull(match);
		checkState(!this.match.isPresent(), "Connect with map can only be called once.");
		this.match = Optional.of(match);
	}

	private int calculateTickOfWorld(final World world) {
		int tick = 0;
		for (WVWObjective objective : this.getObjectives()) {
			if (world.equals(objective.getOwner().orNull())) {
				tick += objective.getType().getPoints();
			}
		}
		return tick;
	}

	@Override
	public int calculateGreenTick() {
		int tick = 0;
		final Optional<WVWMatch> match = this.getMatch();
		if (match.isPresent()) {
			tick = this.calculateTickOfWorld(match.get().getGreenWorld());
		}
		return tick;
	}

	@Override
	public int calculateBlueTick() {
		int tick = 0;
		final Optional<WVWMatch> match = this.getMatch();
		if (match.isPresent()) {
			tick = this.calculateTickOfWorld(match.get().getBlueWorld());
		}
		return tick;
	}

	@Override
	public int calculateRedTick() {
		int tick = 0;
		final Optional<WVWMatch> match = this.getMatch();
		if (match.isPresent()) {
			tick = this.calculateTickOfWorld(match.get().getRedWorld());
		}
		return tick;
	}

}
