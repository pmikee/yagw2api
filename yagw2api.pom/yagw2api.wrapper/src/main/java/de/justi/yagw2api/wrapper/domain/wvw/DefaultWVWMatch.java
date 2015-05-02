package de.justi.yagw2api.wrapper.domain.wvw;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.arenanet.dto.DTOConstants;
import de.justi.yagw2api.arenanet.dto.world.WorldNameDTO;
import de.justi.yagw2api.arenanet.dto.wvw.WVWMapDTO;
import de.justi.yagw2api.arenanet.dto.wvw.WVWMatchDTO;
import de.justi.yagw2api.arenanet.dto.wvw.WVWMatchDetailsDTO;
import de.justi.yagw2api.arenanet.dto.wvw.WVWObjectiveDTO;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.domain.DomainFactory;
import de.justi.yagw2api.wrapper.domain.world.World;
import de.justi.yagwapi.common.AbstractHasChannel;
import de.justi.yagwapi.common.Event;
import de.justi.yagwapi.common.Unmodifiable;

final class DefaultWVWMatch extends AbstractHasChannel implements WVWMatch {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWVWMatch.class);
	private static final WVWDomainFactory WVW_MODEL_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWModelFactory();
	private static final DomainFactory MODEL_FACTORY = YAGW2APIWrapper.INSTANCE.getModelFactory();

	final class UnmodifiableWVWMatch implements WVWMatch, Unmodifiable {

		@Override
		public Set<World> searchWorldsByNamePattern(final Pattern searchPattern) {
			final Collection<World> result = new ArrayList<World>();
			for (World world : DefaultWVWMatch.this.searchWorldsByNamePattern(searchPattern)) {
				result.add(world.createUnmodifiableReference());
			}
			return ImmutableSet.copyOf(result);
		}

		@Override
		public String getId() {
			return DefaultWVWMatch.this.getId();
		}

		@Override
		public World[] getWorlds() {
			final java.util.List<World> buffer = new ArrayList<World>();
			for (World world : DefaultWVWMatch.this.getWorlds()) {
				buffer.add(world.createUnmodifiableReference());
			}
			return buffer.toArray(new World[0]);
		}

		@Override
		public World getRedWorld() {
			return DefaultWVWMatch.this.getRedWorld().createUnmodifiableReference();
		}

		@Override
		public World getGreenWorld() {
			return DefaultWVWMatch.this.getGreenWorld().createUnmodifiableReference();
		}

		@Override
		public World getBlueWorld() {
			return DefaultWVWMatch.this.getBlueWorld().createUnmodifiableReference();
		}

		@Override
		public Optional<World> getWorldByDTOOwnerString(final String dtoOwnerString) {
			final Optional<World> buffer = DefaultWVWMatch.this.getWorldByDTOOwnerString(dtoOwnerString);
			return buffer.isPresent() ? Optional.of(buffer.get().createUnmodifiableReference()) : buffer;
		}

		@Override
		public WVWMap getCenterMap() {
			return DefaultWVWMatch.this.getCenterMap().createUnmodifiableReference();
		}

		@Override
		public WVWMap getBlueMap() {
			return DefaultWVWMatch.this.getBlueMap().createUnmodifiableReference();
		}

		@Override
		public WVWMap getRedMap() {
			return DefaultWVWMatch.this.getRedMap().createUnmodifiableReference();
		}

		@Override
		public WVWMap getGreenMap() {
			return DefaultWVWMatch.this.getGreenMap().createUnmodifiableReference();
		}

		@Override
		public WVWScores getScores() {
			return DefaultWVWMatch.this.getScores().createUnmodifiableReference();
		}

		@Override
		public WVWMatch createUnmodifiableReference() {
			return this;
		}

		@Override
		public EventBus getChannel() {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + Unmodifiable.class.getSimpleName()
					+ " and therefore can not be modified.");
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).addValue(DefaultWVWMatch.this.toString()).toString();
		}

		@Override
		public int hashCode() {
			return DefaultWVWMatch.this.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			return DefaultWVWMatch.this.equals(obj);
		}

		@Override
		public LocalDateTime getStartTimestamp() {
			return DefaultWVWMatch.this.getStartTimestamp();
		}

		@Override
		public LocalDateTime getEndTimestamp() {
			return DefaultWVWMatch.this.getEndTimestamp();
		}

		@Override
		public int calculateGreenTick() {
			return DefaultWVWMatch.this.calculateGreenTick();
		}

		@Override
		public int calculateBlueTick() {
			return DefaultWVWMatch.this.calculateBlueTick();
		}

		@Override
		public int calculateRedTick() {
			return DefaultWVWMatch.this.calculateRedTick();
		}
	}

	public static class DefaultWVWMatchBuilder implements WVWMatch.WVWMatchBuilder {
		private Optional<WVWMatchDTO> fromMatchDTO = Optional.absent();
		private Optional<String> id = Optional.absent();
		private Optional<WVWMap> centerMap = Optional.absent();
		private Optional<WVWMap> redMap = Optional.absent();
		private Optional<WVWMap> greenMap = Optional.absent();
		private Optional<WVWMap> blueMap = Optional.absent();
		private Optional<World> redWorld = Optional.absent();
		private Optional<World> greenWorld = Optional.absent();
		private Optional<World> blueWorld = Optional.absent();
		private Optional<Integer> redScore = Optional.absent();
		private Optional<Integer> greenScore = Optional.absent();
		private Optional<Integer> blueScore = Optional.absent();
		private Optional<LocalDateTime> start = Optional.absent();
		private Optional<LocalDateTime> end = Optional.absent();

		@Override
		public WVWMatch build() {
			checkState(this.id.isPresent(), "Missing id in " + this);
			checkState(this.centerMap != null, "Missing center " + WVWMap.class.getSimpleName() + " in " + this);
			checkState(this.centerMap.isPresent(), "Missing center " + WVWMap.class.getSimpleName() + " in " + this);
			checkState(this.redMap != null, "Missing red " + WVWMap.class.getSimpleName() + " in " + this);
			checkState(this.redMap.isPresent(), "Missing red " + WVWMap.class.getSimpleName() + " in " + this);
			checkState(this.greenMap != null, "Missing green " + WVWMap.class.getSimpleName() + " in " + this);
			checkState(this.greenMap.isPresent(), "Missing green " + WVWMap.class.getSimpleName() + " in " + this);
			checkState(this.blueMap != null, "Missing blue " + WVWMap.class.getSimpleName() + " in " + this);
			checkState(this.blueMap.isPresent(), "Missing blue " + WVWMap.class.getSimpleName() + " in " + this);
			checkState(this.redWorld != null, "Missing red " + World.class.getSimpleName() + " in " + this);
			checkState(this.redWorld.isPresent(), "Missing red " + World.class.getSimpleName() + " in " + this);
			checkState(this.greenWorld != null, "Missing green " + World.class.getSimpleName() + " in " + this);
			checkState(this.greenWorld.isPresent(), "Missing green " + World.class.getSimpleName() + " in " + this);
			checkState(this.blueWorld != null, "Missing blue " + World.class.getSimpleName() + " in " + this);
			checkState(this.blueWorld.isPresent(), "Missing blue " + World.class.getSimpleName() + " in " + this);
			checkState(this.start.isPresent(), "Missing start in " + this);
			checkState(this.end.isPresent(), "Missing start in " + this);

			final WVWMatch match = new DefaultWVWMatch(this.id.get(), this.redWorld.get(), this.greenWorld.get(), this.blueWorld.get(), this.centerMap.get(), this.redMap.get(),
					this.greenMap.get(), this.blueMap.get(), this.start.get(), this.end.get());
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

		private void setupOwner(final WVWMatch match, final WVWMap map, final WVWMatchDTO matchDTO, final WVWMapDTO mapDTO) {
			checkNotNull(match);
			checkNotNull(map);
			checkNotNull(matchDTO);

			for (WVWObjectiveDTO objectiveDTO : mapDTO.getObjectives()) {
				if (objectiveDTO.getOwner() != null) {
					final Optional<World> owner = match.getWorldByDTOOwnerString(objectiveDTO.getOwner());

					final Optional<WVWObjective> objective = map.getByObjectiveId(objectiveDTO.getId());
					checkState(objective.isPresent());

					objective.get().initializeOwner(owner.orNull());
				}
			}
		}

		private static final class BuildMapFromDTOAction implements Callable<Void> {
			private final WVWMapDTO dto;
			private Optional<WVWMap> result = Optional.absent();

			public BuildMapFromDTOAction(final WVWMapDTO dto) {
				this.dto = checkNotNull(dto);
			}

			@Override
			public String toString() {
				return MoreObjects.toStringHelper(this).add("dto", this.dto).add("resultPresent", this.result.isPresent()).toString();
			}

			@Override
			public Void call() throws Exception {
				try {
					this.result = Optional.of(WVW_MODEL_FACTORY.newMapBuilder().fromDTO(this.dto).build());
				} catch (Exception e) {
					LOGGER.error("Uncought exception thrown during execution of {}", this, e);
					throw e;
				}
				return null;
			}

			public Optional<WVWMap> getResult() {
				return this.result;
			}
		}

		private static final class BuildWorldFromDTOAction implements Callable<Void> {
			private final WorldNameDTO dto;
			private Optional<World> result = Optional.absent();

			public BuildWorldFromDTOAction(final WorldNameDTO dto) {
				this.dto = checkNotNull(dto);
			}

			@Override
			public String toString() {
				return MoreObjects.toStringHelper(this).add("dto", this.dto).add("resultPresent", this.result.isPresent()).toString();
			}

			@Override
			public Void call() throws Exception {
				try {
					this.result = Optional.of(MODEL_FACTORY.newWorldBuilder().fromDTO(this.dto).build());
				} catch (Exception e) {
					LOGGER.error("Uncought exception thrown during execution of {}", this, e);
					throw e;
				}
				return null;
			}

			public Optional<World> getResult() {
				return this.result;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public WVWMatch.WVWMatchBuilder fromMatchDTO(final WVWMatchDTO dto, final Locale locale) {
			checkNotNull(locale);
			checkState(!this.fromMatchDTO.isPresent());

			final long startTimestamp = System.currentTimeMillis();
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Going to fill " + this.getClass().getSimpleName() + " with data from dto=" + dto + " and locale=" + locale);
			}

			this.fromMatchDTO = Optional.of(dto);
			this.id = Optional.of(dto.getId());

			checkArgument(dto.getDetails().get().getCenterMap().getType().equalsIgnoreCase(DTOConstants.CENTER_MAP_TYPE_STRING));
			checkArgument(dto.getDetails().get().getRedMap().getType().equalsIgnoreCase(DTOConstants.RED_MAP_TYPE_STRING));
			checkArgument(dto.getDetails().get().getGreenMap().getType().equalsIgnoreCase(DTOConstants.GREEN_MAP_TYPE_STRING));
			checkArgument(dto.getDetails().get().getBlueMap().getType().equalsIgnoreCase(DTOConstants.BLUE_MAP_TYPE_STRING));

			final BuildMapFromDTOAction buildCenterMapAction = new BuildMapFromDTOAction(dto.getDetails().get().getCenterMap());
			final BuildMapFromDTOAction buildRedMapAction = new BuildMapFromDTOAction(dto.getDetails().get().getRedMap());
			final BuildMapFromDTOAction buildGreenMapAction = new BuildMapFromDTOAction(dto.getDetails().get().getGreenMap());
			final BuildMapFromDTOAction buildBlueMapAction = new BuildMapFromDTOAction(dto.getDetails().get().getBlueMap());
			final BuildWorldFromDTOAction buildRedWorldAction = new BuildWorldFromDTOAction(dto.getRedWorldName(YAGW2APIArenanet.INSTANCE.getCurrentLocale()).get());
			final BuildWorldFromDTOAction buildGreenWorldAction = new BuildWorldFromDTOAction(dto.getGreenWorldName(YAGW2APIArenanet.INSTANCE.getCurrentLocale()).get());
			final BuildWorldFromDTOAction buildBlueWorldAction = new BuildWorldFromDTOAction(dto.getBlueWorldName(YAGW2APIArenanet.INSTANCE.getCurrentLocale()).get());

			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Going to execute " + BuildMapFromDTOAction.class.getSimpleName() + "'s and " + BuildWorldFromDTOAction.class.getSimpleName() + "'s to build "
						+ this.getClass().getSimpleName() + "\n" + "~ " + buildCenterMapAction.toString() + "\n" + "~ " + buildRedMapAction.toString() + "\n" + "~ "
						+ buildGreenMapAction.toString() + "\n" + "~ " + buildBlueMapAction.toString() + "\n" + "~ " + buildRedWorldAction.toString() + "\n" + "~ "
						+ buildGreenWorldAction.toString() + "\n" + "~ " + buildBlueWorldAction.toString());
			}

			YAGW2APIWrapper.INSTANCE.getForkJoinPool().invokeAll(
					Lists.newArrayList(buildCenterMapAction, buildRedMapAction, buildBlueMapAction, buildGreenMapAction, buildRedWorldAction, buildGreenWorldAction,
							buildBlueWorldAction));

			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Done with execution of " + BuildMapFromDTOAction.class.getSimpleName() + "'s and " + BuildWorldFromDTOAction.class.getSimpleName() + "'s to build "
						+ this.getClass().getSimpleName() + "\n" + "~ " + buildCenterMapAction.toString() + "\n" + "~ " + buildRedMapAction.toString() + "\n" + "~ "
						+ buildGreenMapAction.toString() + "\n" + "~ " + buildBlueMapAction.toString() + "\n" + "~ " + buildRedWorldAction.toString() + "\n" + "~ "
						+ buildGreenWorldAction.toString() + "\n" + "~ " + buildBlueWorldAction.toString());
			}

			this.centerMap = buildCenterMapAction.getResult();
			checkState(this.centerMap != null, "Failed to build center " + WVWMap.class.getSimpleName() + " " + dto.getId());
			this.blueMap = buildBlueMapAction.getResult();
			checkState(this.blueMap != null, "Failed to build blue " + WVWMap.class.getSimpleName() + " " + dto.getId());
			this.greenMap = buildGreenMapAction.getResult();
			checkState(this.greenMap != null, "Failed to build green " + WVWMap.class.getSimpleName() + " " + dto.getId());
			this.redMap = buildRedMapAction.getResult();
			checkState(this.redMap != null, "Failed to build red " + WVWMap.class.getSimpleName() + " " + dto.getId());

			this.redWorld = buildRedWorldAction.getResult();
			checkState(this.redWorld != null, "Failed to build red " + World.class.getSimpleName() + " id=" + dto.getRedWorldId());
			this.greenWorld = buildGreenWorldAction.getResult();
			checkState(this.greenWorld != null, "Failed to build green " + World.class.getSimpleName() + " id=" + dto.getRedWorldId());
			this.blueWorld = buildBlueWorldAction.getResult();
			checkState(this.blueWorld != null, "Failed to build blue " + World.class.getSimpleName() + " id=" + dto.getRedWorldId());

			this.start(dto.getStartTime());
			this.end(dto.getEndTime());

			final Optional<WVWMatchDetailsDTO> details = dto.getDetails();
			if (details.isPresent()) {
				this.redScore(details.get().getRedScore());
				this.blueScore(details.get().getBlueScore());
				this.greenScore(details.get().getGreenScore());
			}

			final long endTimestamp = System.currentTimeMillis();
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Done with filling " + this.getClass().getSimpleName() + " with data from dto=" + dto + " and locale=" + locale + " in "
						+ (endTimestamp - startTimestamp) + "ms");
			}

			return this;
		}

		@Override
		public WVWMatch.WVWMatchBuilder redScore(final int score) {
			checkArgument(score > 0);
			this.redScore = Optional.of(score);
			return this;
		}

		@Override
		public WVWMatch.WVWMatchBuilder blueScore(final int score) {
			checkArgument(score > 0);
			this.blueScore = Optional.of(score);
			return this;
		}

		@Override
		public WVWMatch.WVWMatchBuilder greenScore(final int score) {
			checkArgument(score > 0);
			this.greenScore = Optional.of(score);
			return this;
		}

		@Override
		public WVWMatchBuilder start(final LocalDateTime date) {
			this.start = Optional.fromNullable(date);
			return this;
		}

		@Override
		public WVWMatchBuilder end(final LocalDateTime date) {
			this.end = Optional.fromNullable(date);
			return this;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("id", this.id).add("start", this.start).add("end", this.end).add("redScore", this.redScore)
					.add("greenScore", this.greenScore).add("blueScore", this.blueScore).add("redWorld", this.redWorld).add("greenWorld", this.greenWorld)
					.add("blueWorld", this.blueWorld).add("centerMap", this.centerMap).add("redMap", this.redMap).add("greenMap", this.greenMap).add("blueMap", this.blueMap)
					.toString();
		}

	}

	private final String id;
	private final World redWorld;
	private final World greenWorld;
	private final World blueWorld;
	private final WVWMap centerMap;
	private final WVWMap redMap;
	private final WVWMap greenMap;
	private final WVWMap blueMap;
	private final WVWScores scores;
	private final LocalDateTime startTime;
	private final LocalDateTime endTime;

	private DefaultWVWMatch(final String id, final World redWorld, final World greenWorld, final World blueWorld, final WVWMap centerMap, final WVWMap redMap,
			final WVWMap greenMap, final WVWMap blueMap, final LocalDateTime start, final LocalDateTime end) {
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

		// start / end time
		this.startTime = checkNotNull(start);
		this.endTime = checkNotNull(end);
	}

	@Subscribe
	public void onEvent(final Event event) {
		LOGGER.trace("{} is going to forward {}", event);
		this.getChannel().post(event);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public World[] getWorlds() {
		return new World[] { this.redWorld, this.greenWorld, this.blueWorld };
	}

	@Override
	public World getRedWorld() {
		return this.redWorld;
	}

	@Override
	public World getGreenWorld() {
		return this.greenWorld;
	}

	@Override
	public World getBlueWorld() {
		return this.blueWorld;
	}

	@Override
	public WVWMap getCenterMap() {
		return this.centerMap;
	}

	@Override
	public WVWMap getBlueMap() {
		return this.blueMap;
	}

	@Override
	public WVWMap getRedMap() {
		return this.redMap;
	}

	@Override
	public WVWMap getGreenMap() {
		return this.greenMap;
	}

	@Override
	public WVWScores getScores() {
		return this.scores;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("scores", this.scores).add("redWorld", this.redWorld).add("greenWorld", this.greenWorld)
				.add("blueWorld", this.blueWorld).add("centerMap", this.centerMap).add("redMap", this.redMap).add("greenMap", this.greenMap).add("blueMap", this.blueMap)
				.add("start", this.startTime).add("end", this.endTime).toString();
	}

	@Override
	public Optional<World> getWorldByDTOOwnerString(final String dtoOwnerString) {
		checkNotNull(dtoOwnerString);
		switch (dtoOwnerString.toUpperCase()) {
			case DTOConstants.OWNER_RED_STRING:
				return Optional.of(this.redWorld);
			case DTOConstants.OWNER_GREEN_STRING:
				return Optional.of(this.greenWorld);
			case DTOConstants.OWNER_BLUE_STRING:
				return Optional.of(this.blueWorld);
			case DTOConstants.OWNER_NEUTRAL_STRING:
				return Optional.absent();
			default:
				LOGGER.error("Invalid dtoOwnerString: " + dtoOwnerString);
				return Optional.absent();
		}
	}

	@Override
	public Set<World> searchWorldsByNamePattern(final Pattern searchPattern) {
		checkNotNull(searchPattern);
		checkState(this.redWorld != null);
		checkState(this.greenWorld != null);
		checkState(this.blueWorld != null);
		final Set<World> result = new HashSet<World>();
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
	public WVWMatch createUnmodifiableReference() {
		return new UnmodifiableWVWMatch();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getClass().getName(), this.id);
	}

	@Override
	public boolean equals(final Object obj) {
		if ((obj == null) || !(obj instanceof WVWMatch)) {
			return false;
		} else {
			final WVWMatch match = (WVWMatch) obj;
			return Objects.equal(this.id, match.getId());
		}
	}

	@Override
	public LocalDateTime getStartTimestamp() {
		return this.startTime;
	}

	@Override
	public LocalDateTime getEndTimestamp() {
		return this.endTime;
	}

	@Override
	public int calculateGreenTick() {
		return this.getGreenMap().calculateGreenTick() + this.getBlueMap().calculateGreenTick() + this.getRedMap().calculateGreenTick() + this.getCenterMap().calculateGreenTick();
	}

	@Override
	public int calculateBlueTick() {
		return this.getGreenMap().calculateBlueTick() + this.getBlueMap().calculateBlueTick() + this.getRedMap().calculateBlueTick() + this.getCenterMap().calculateBlueTick();
	}

	@Override
	public int calculateRedTick() {
		return this.getGreenMap().calculateRedTick() + this.getBlueMap().calculateRedTick() + this.getRedMap().calculateRedTick() + this.getCenterMap().calculateRedTick();
	}
}
