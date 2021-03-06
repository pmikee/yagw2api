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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.v1.YAGW2APIArenanetV1;
import de.justi.yagw2api.arenanet.v1.dto.guild.GuildDetailsDTO;
import de.justi.yagw2api.arenanet.v1.dto.wvw.WVWObjectiveDTO;
import de.justi.yagw2api.common.event.AbstractHasChannel;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.guild.GuildWrapper;
import de.justi.yagw2api.wrapper.guild.domain.Guild;
import de.justi.yagw2api.wrapper.guild.domain.NoSuchGuildException;
import de.justi.yagw2api.wrapper.world.domain.World;
import de.justi.yagw2api.wrapper.wvw.domain.WVWLocationType;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMap;
import de.justi.yagw2api.wrapper.wvw.domain.WVWObjective;
import de.justi.yagw2api.wrapper.wvw.domain.WVWObjectiveType;
import de.justi.yagw2api.wrapper.wvw.event.WVWEventFactory;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveUnclaimedEvent;

final class DefaultWVWObjective extends AbstractHasChannel implements WVWObjective {
	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWVWObjective.class);
	private static final WVWEventFactory WVW_MODEL_EVENTS_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWDomainEventFactory();

	// EMBEDDED
	final class UnmodifiableWVWObjective implements WVWObjective {
		// CONSTS
		// EMBEDDED
		// FIELDS
		// CONSTRUCTOR
		// METHODS
		@Override
		public WVWLocationType getLocation() {
			return DefaultWVWObjective.this.getLocation();
		}

		@Override
		public WVWObjective createUnmodifiableReference() {
			return this;
		}

		@Override
		public EventBus getChannel() {
			throw new UnsupportedOperationException("unmodifiable");
		}

		@Override
		public Optional<String> getLabel() {
			return DefaultWVWObjective.this.getLabel();
		}

		@Override
		public WVWObjectiveType getType() {
			return DefaultWVWObjective.this.getType();
		}

		@Override
		public Optional<World> getOwner() {
			final Optional<World> buffer = DefaultWVWObjective.this.getOwner();
			return buffer.isPresent() ? Optional.of(buffer.get().createUnmodifiableReference()) : buffer;
		}

		@Override
		public void capture(final World capturingWorld) {
			throw new UnsupportedOperationException("unmodifiable");
		}

		@Override
		public long getRemainingBuffDuration(final TimeUnit unit) {
			return DefaultWVWObjective.this.getRemainingBuffDuration(unit);
		}

		@Override
		public Optional<WVWMap> getMap() {
			final Optional<WVWMap> buffer = DefaultWVWObjective.this.getMap();
			return buffer.isPresent() ? Optional.of(buffer.get().createUnmodifiableReference()) : buffer;
		}

		@Override
		public void connectWithMap(final WVWMap map) {
			throw new UnsupportedOperationException("unmodifiable");
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).addValue(DefaultWVWObjective.this.toString()).toString();
		}

		@Override
		public void updateOnSynchronization() {
			throw new UnsupportedOperationException("unmodifiable");
		}

		@Override
		public void initializeOwner(final World owningWorld) {
			throw new UnsupportedOperationException("unmodifiable");
		}

		@Override
		public Optional<LocalDateTime> getEndOfBuffTimestamp() {
			return DefaultWVWObjective.this.getEndOfBuffTimestamp();
		}

		@Override
		public int hashCode() {
			return DefaultWVWObjective.this.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			return DefaultWVWObjective.this.equals(obj);
		}

		@Override
		public Optional<Guild> getClaimedByGuild() {
			return DefaultWVWObjective.this.getClaimedByGuild();
		}

		@Override
		public void claim(final Guild guild) {
			throw new UnsupportedOperationException("unmodifiable");
		}

		@Override
		public void initializeClaimedByGuild(final Guild guild) {
			throw new UnsupportedOperationException("unmodifiable");
		}
	}

	public static class DefaultWVWObjectiveBuilder implements WVWObjective.WVWObjectiveBuilder {

		// CONSTS
		// EMBEDDED
		// FIELDS
		private Optional<WVWLocationType> location = Optional.absent();
		private Optional<World> owner = Optional.absent();
		private Optional<WVWMap> map = Optional.absent();
		private Optional<Guild> claimedByGuild = Optional.absent();

		private final GuildWrapper guildWrapper;

		// CONSTRUCTOR
		@Inject
		public DefaultWVWObjectiveBuilder(final GuildWrapper guildWrapper) {
			this.guildWrapper = checkNotNull(guildWrapper, "missing guildWrapper");
		}

		// METHODS
		@Override
		public WVWObjective build() {
			checkState(this.location.isPresent());
			checkState(this.map != null);

			final WVWObjective result = new DefaultWVWObjective(this.location.get());
			if (this.map.isPresent()) {
				result.connectWithMap(this.map.get());
			}
			result.initializeOwner(this.owner.orNull());
			result.initializeClaimedByGuild(this.claimedByGuild.orNull());
			return result;
		}

		@Override
		public WVWObjective.WVWObjectiveBuilder fromDTO(final WVWObjectiveDTO dto) {
			checkNotNull(dto);
			final Optional<GuildDetailsDTO> guildDetails = dto.getGuildDetails();
			if (guildDetails.isPresent()) {
				try {
					final Guild guild = this.guildWrapper.getGuild(guildDetails.get().getId());
					this.claimedBy(guild);
				} catch (NoSuchGuildException e) {
					Throwables.propagate(e);
				}
			}
			return this.location(DefaultWVWLocationType.forObjectiveId(dto.getId()).get());
		}

		@Override
		public WVWObjective.WVWObjectiveBuilder location(final WVWLocationType location) {
			this.location = Optional.fromNullable(location);
			return this;
		}

		@Override
		public WVWObjective.WVWObjectiveBuilder owner(final World world) {
			this.owner = Optional.fromNullable(world);
			return this;
		}

		@Override
		public WVWObjectiveBuilder map(final WVWMap map) {
			this.map = Optional.fromNullable(map);
			return this;
		}

		@Override
		public WVWObjectiveBuilder claimedBy(final Guild guild) {
			this.claimedByGuild = Optional.fromNullable(guild);
			return this;
		}
	}

	private final WVWLocationType location;
	private Optional<World> owningWorld = Optional.absent();
	private Optional<LocalDateTime> lastCaptureEventTimestamp = Optional.absent();
	private boolean postedEndOfBuffEvent = true;
	private Optional<Guild> claimedByGuild = Optional.absent();
	private Optional<WVWMap> map = Optional.absent();

	private DefaultWVWObjective(final WVWLocationType location) {
		checkNotNull(location);
		checkArgument(location.getObjectiveId().isPresent());
		checkArgument(location.getObjectiveType().isPresent());
		this.location = location;
	}

	@Override
	public Optional<String> getLabel() {
		return this.location.getLabel(YAGW2APIArenanetV1.INSTANCE.getCurrentLocale());
	}

	@Override
	public WVWObjectiveType getType() {
		return this.location.getObjectiveType().get();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("type", this.getType()).add("name", this.getLabel()).add("location", this.location).toString();
	}

	@Override
	public WVWLocationType getLocation() {
		return this.location;
	}

	@Override
	public void initializeOwner(final World owningWorld) {
		checkState(!this.owningWorld.isPresent());
		checkState(!this.lastCaptureEventTimestamp.isPresent());
		checkState(this.postedEndOfBuffEvent);
		this.owningWorld = Optional.fromNullable(owningWorld);
	}

	@Override
	public long getRemainingBuffDuration(final TimeUnit unit) {
		final LocalDateTime now = LocalDateTime.now();
		final Duration duration = Duration.between(this.lastCaptureEventTimestamp.or(now), now);
		return unit.convert(this.getType().getBuffDuration(TimeUnit.SECONDS) - duration.get(ChronoUnit.SECONDS), TimeUnit.SECONDS);
	}

	@Override
	public Optional<World> getOwner() {
		return this.owningWorld;
	}

	@Override
	public Optional<WVWMap> getMap() {
		return this.map;
	}

	@Override
	public void connectWithMap(final WVWMap map) {
		checkNotNull(map);
		checkState(!this.map.isPresent(), "Connect with map can only be called once.");
		this.map = Optional.of(map);
	}

	@Override
	public WVWObjective createUnmodifiableReference() {
		return new UnmodifiableWVWObjective();
	}

	@Override
	public void updateOnSynchronization() {
		final long remainingBuffMillis = this.getRemainingBuffDuration(TimeUnit.MILLISECONDS);
		if (!this.postedEndOfBuffEvent && (remainingBuffMillis == 0)) {
			synchronized (this) {
				if (!this.postedEndOfBuffEvent) {
					this.postedEndOfBuffEvent = true;
					this.getChannel().post(WVW_MODEL_EVENTS_FACTORY.newObjectiveEndOfBuffEvent(this));
				}
			}
		} else if ((remainingBuffMillis > 0) && LOGGER.isTraceEnabled()) {
			LOGGER.trace("Remaining buff duration for {} is {}ms", this, remainingBuffMillis);
		}
	}

	@Override
	public Optional<LocalDateTime> getEndOfBuffTimestamp() {
		if (this.lastCaptureEventTimestamp.isPresent()) {
			final LocalDateTime timestamp = this.lastCaptureEventTimestamp.get().plus(this.getType().getBuffDuration(TimeUnit.SECONDS), ChronoUnit.SECONDS);
			return Optional.of(timestamp);
		} else {
			return Optional.absent();
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getClass().getName(), this.getLocation().getObjectiveId().orNull());
	}

	@Override
	public boolean equals(final Object obj) {
		if ((obj == null) || !(obj instanceof WVWObjective)) {
			return false;
		} else {
			final WVWObjective objective = (WVWObjective) obj;
			return Objects.equal(this.getLocation().getObjectiveId().orNull(), objective.getLocation().getObjectiveId().orNull());
		}
	}

	@Override
	public Optional<Guild> getClaimedByGuild() {
		return this.claimedByGuild;
	}

	@Override
	public void claim(final Guild guild) {
		if ((guild != null) && (!this.claimedByGuild.isPresent() || !this.claimedByGuild.get().equals(guild))) {
			// changed claiming guild
			final WVWObjectiveClaimedEvent event = WVW_MODEL_EVENTS_FACTORY.newObjectiveClaimedEvent(this, guild, this.claimedByGuild);
			LOGGER.debug("{} has been claimed by {}", this, guild);
			this.claimedByGuild = Optional.of(guild);
			this.getChannel().post(event);
		} else if ((guild == null) && this.claimedByGuild.isPresent()) {
			// unclaim (e.g. after capture)
			final WVWObjectiveUnclaimedEvent event = WVW_MODEL_EVENTS_FACTORY.newObjectiveUnclaimedEvent(this, this.claimedByGuild);
			LOGGER.debug("{} was unclaimed from {}", this, this.claimedByGuild);
			this.claimedByGuild = Optional.absent();
			this.getChannel().post(event);
		} else {
			// no change
			LOGGER.trace("{} has already been claimed by {}", this, guild);
		}
	}

	@Override
	public void capture(final World capturingWorld) {
		if ((capturingWorld != null) && (!this.owningWorld.isPresent() || !this.owningWorld.get().equals(capturingWorld))) {
			// changed owning world
			final WVWObjectiveCaptureEvent event = WVW_MODEL_EVENTS_FACTORY.newObjectiveCapturedEvent(this, capturingWorld, this.owningWorld);
			LOGGER.debug("{} has captured {} when expected remaining buff duration was {}s", capturingWorld, this, this.getRemainingBuffDuration(TimeUnit.SECONDS));
			this.owningWorld = Optional.of(capturingWorld);
			this.lastCaptureEventTimestamp = Optional.of(event.getTimestamp());
			this.postedEndOfBuffEvent = false;
			this.getChannel().post(event);
		} else {
			// no change
			LOGGER.trace("{} has already been captured by {}", this, capturingWorld);
		}
	}

	@Override
	public void initializeClaimedByGuild(final Guild guild) {
		this.claimedByGuild = Optional.fromNullable(guild);
	}

}
