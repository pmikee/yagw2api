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
import de.justi.yagw2api.wrapper.domain.IModelFactory;
import de.justi.yagw2api.wrapper.domain.world.IWorld;
import de.justi.yagwapi.common.AbstractHasChannel;
import de.justi.yagwapi.common.IEvent;
import de.justi.yagwapi.common.IUnmodifiable;

final class WVWMatch extends AbstractHasChannel implements IWVWMatch {
	private static final Logger LOGGER = LoggerFactory.getLogger(WVWMatch.class);
	private static final IWVWModelFactory WVW_MODEL_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWModelFactory();
	private static final IModelFactory MODEL_FACTORY = YAGW2APIWrapper.INSTANCE.getModelFactory();

	final class UnmodifiableWVWMatch implements IWVWMatch, IUnmodifiable {

		@Override
		public Set<IWorld> searchWorldsByNamePattern(final Pattern searchPattern) {
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
		public Optional<IWorld> getWorldByDTOOwnerString(final String dtoOwnerString) {
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
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName()
					+ " and therefore can not be modified.");
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).addValue(WVWMatch.this.toString()).toString();
		}

		@Override
		public int hashCode() {
			return WVWMatch.this.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			return WVWMatch.this.equals(obj);
		}

		@Override
		public LocalDateTime getStartTimestamp() {
			return WVWMatch.this.getStartTimestamp();
		}

		@Override
		public LocalDateTime getEndTimestamp() {
			return WVWMatch.this.getEndTimestamp();
		}

		@Override
		public int calculateGreenTick() {
			return WVWMatch.this.calculateGreenTick();
		}

		@Override
		public int calculateBlueTick() {
			return WVWMatch.this.calculateBlueTick();
		}

		@Override
		public int calculateRedTick() {
			return WVWMatch.this.calculateRedTick();
		}
	}

	public static class WVWMatchBuilder implements IWVWMatch.IWVWMatchBuilder {
		private Optional<WVWMatchDTO> fromMatchDTO = Optional.absent();
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
		private Optional<LocalDateTime> start = Optional.absent();
		private Optional<LocalDateTime> end = Optional.absent();

		@Override
		public IWVWMatch build() {
			checkState(this.id.isPresent(), "Missing id in " + this);
			checkState(this.centerMap != null, "Missing center " + IWVWMap.class.getSimpleName() + " in " + this);
			checkState(this.centerMap.isPresent(), "Missing center " + IWVWMap.class.getSimpleName() + " in " + this);
			checkState(this.redMap != null, "Missing red " + IWVWMap.class.getSimpleName() + " in " + this);
			checkState(this.redMap.isPresent(), "Missing red " + IWVWMap.class.getSimpleName() + " in " + this);
			checkState(this.greenMap != null, "Missing green " + IWVWMap.class.getSimpleName() + " in " + this);
			checkState(this.greenMap.isPresent(), "Missing green " + IWVWMap.class.getSimpleName() + " in " + this);
			checkState(this.blueMap != null, "Missing blue " + IWVWMap.class.getSimpleName() + " in " + this);
			checkState(this.blueMap.isPresent(), "Missing blue " + IWVWMap.class.getSimpleName() + " in " + this);
			checkState(this.redWorld != null, "Missing red " + IWorld.class.getSimpleName() + " in " + this);
			checkState(this.redWorld.isPresent(), "Missing red " + IWorld.class.getSimpleName() + " in " + this);
			checkState(this.greenWorld != null, "Missing green " + IWorld.class.getSimpleName() + " in " + this);
			checkState(this.greenWorld.isPresent(), "Missing green " + IWorld.class.getSimpleName() + " in " + this);
			checkState(this.blueWorld != null, "Missing blue " + IWorld.class.getSimpleName() + " in " + this);
			checkState(this.blueWorld.isPresent(), "Missing blue " + IWorld.class.getSimpleName() + " in " + this);
			checkState(this.start.isPresent(), "Missing start in " + this);
			checkState(this.end.isPresent(), "Missing start in " + this);

			final IWVWMatch match = new WVWMatch(this.id.get(), this.redWorld.get(), this.greenWorld.get(), this.blueWorld.get(), this.centerMap.get(), this.redMap.get(),
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

		private void setupOwner(final IWVWMatch match, final IWVWMap map, final WVWMatchDTO matchDTO, final WVWMapDTO mapDTO) {
			checkNotNull(match);
			checkNotNull(map);
			checkNotNull(matchDTO);

			for (WVWObjectiveDTO objectiveDTO : mapDTO.getObjectives()) {
				if (objectiveDTO.getOwner() != null) {
					final Optional<IWorld> owner = match.getWorldByDTOOwnerString(objectiveDTO.getOwner());

					final Optional<IWVWObjective> objective = map.getByObjectiveId(objectiveDTO.getId());
					checkState(objective.isPresent());

					objective.get().initializeOwner(owner.orNull());
				}
			}
		}

		private static final class BuildMapFromDTOAction implements Callable<Void> {
			private final WVWMapDTO dto;
			private Optional<IWVWMap> result = Optional.absent();

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

			public Optional<IWVWMap> getResult() {
				return this.result;
			}
		}

		private static final class BuildWorldFromDTOAction implements Callable<Void> {
			private final WorldNameDTO dto;
			private Optional<IWorld> result = Optional.absent();

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

			public Optional<IWorld> getResult() {
				return this.result;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public IWVWMatch.IWVWMatchBuilder fromMatchDTO(final WVWMatchDTO dto, final Locale locale) {
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
			checkState(this.centerMap != null, "Failed to build center " + IWVWMap.class.getSimpleName() + " " + dto.getId());
			this.blueMap = buildBlueMapAction.getResult();
			checkState(this.blueMap != null, "Failed to build blue " + IWVWMap.class.getSimpleName() + " " + dto.getId());
			this.greenMap = buildGreenMapAction.getResult();
			checkState(this.greenMap != null, "Failed to build green " + IWVWMap.class.getSimpleName() + " " + dto.getId());
			this.redMap = buildRedMapAction.getResult();
			checkState(this.redMap != null, "Failed to build red " + IWVWMap.class.getSimpleName() + " " + dto.getId());

			this.redWorld = buildRedWorldAction.getResult();
			checkState(this.redWorld != null, "Failed to build red " + IWorld.class.getSimpleName() + " id=" + dto.getRedWorldId());
			this.greenWorld = buildGreenWorldAction.getResult();
			checkState(this.greenWorld != null, "Failed to build green " + IWorld.class.getSimpleName() + " id=" + dto.getRedWorldId());
			this.blueWorld = buildBlueWorldAction.getResult();
			checkState(this.blueWorld != null, "Failed to build blue " + IWorld.class.getSimpleName() + " id=" + dto.getRedWorldId());

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
		public IWVWMatch.IWVWMatchBuilder redScore(final int score) {
			checkArgument(score > 0);
			this.redScore = Optional.of(score);
			return this;
		}

		@Override
		public IWVWMatch.IWVWMatchBuilder blueScore(final int score) {
			checkArgument(score > 0);
			this.blueScore = Optional.of(score);
			return this;
		}

		@Override
		public IWVWMatch.IWVWMatchBuilder greenScore(final int score) {
			checkArgument(score > 0);
			this.greenScore = Optional.of(score);
			return this;
		}

		@Override
		public IWVWMatchBuilder start(final LocalDateTime date) {
			this.start = Optional.fromNullable(date);
			return this;
		}

		@Override
		public IWVWMatchBuilder end(final LocalDateTime date) {
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
	private final IWorld redWorld;
	private final IWorld greenWorld;
	private final IWorld blueWorld;
	private final IWVWMap centerMap;
	private final IWVWMap redMap;
	private final IWVWMap greenMap;
	private final IWVWMap blueMap;
	private final IWVWScores scores;
	private final LocalDateTime startTime;
	private final LocalDateTime endTime;

	private WVWMatch(final String id, final IWorld redWorld, final IWorld greenWorld, final IWorld blueWorld, final IWVWMap centerMap, final IWVWMap redMap,
			final IWVWMap greenMap, final IWVWMap blueMap, final LocalDateTime start, final LocalDateTime end) {
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
	public void onEvent(final IEvent event) {
		LOGGER.trace("{} is going to forward {}", event);
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

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("scores", this.scores).add("redWorld", this.redWorld).add("greenWorld", this.greenWorld)
				.add("blueWorld", this.blueWorld).add("centerMap", this.centerMap).add("redMap", this.redMap).add("greenMap", this.greenMap).add("blueMap", this.blueMap)
				.add("start", this.startTime).add("end", this.endTime).toString();
	}

	@Override
	public Optional<IWorld> getWorldByDTOOwnerString(final String dtoOwnerString) {
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
	public Set<IWorld> searchWorldsByNamePattern(final Pattern searchPattern) {
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
		return Objects.hashCode(this.getClass().getName(), this.id);
	}

	@Override
	public boolean equals(final Object obj) {
		if ((obj == null) || !(obj instanceof IWVWMatch)) {
			return false;
		} else {
			final IWVWMatch match = (IWVWMatch) obj;
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
