package de.justi.yagw2api.wrapper.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;

import de.justi.yagw2api.arenanet.IGuildDetailsDTO;
import de.justi.yagw2api.arenanet.IWVWObjectiveDTO;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.wrapper.IGuild;
import de.justi.yagw2api.wrapper.IModelFactory;
import de.justi.yagw2api.wrapper.IWVWLocationType;
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWModelEventFactory;
import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveType;
import de.justi.yagw2api.wrapper.IWVWObjectiveUnclaimedEvent;
import de.justi.yagw2api.wrapper.IWorld;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagwapi.common.AbstractHasChannel;
import de.justi.yagwapi.common.IUnmodifiable;

final class WVWObjective extends AbstractHasChannel implements IWVWObjective {
	private static final Logger LOGGER = Logger.getLogger(WVWObjective.class);
	private static final IWVWModelEventFactory WVW_MODEL_EVENTS_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWModelEventFactory();
	private static final IModelFactory MODEL_FACTORY = YAGW2APIWrapper.INSTANCE.getModelFactory();

	final class UnmodifiableWVWObjective implements IWVWObjective, IUnmodifiable {

		@Override
		public IWVWLocationType getLocation() {
			return WVWObjective.this.getLocation();
		}

		@Override
		public IWVWObjective createUnmodifiableReference() {
			return this;
		}

		@Override
		public EventBus getChannel() {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName() + " and therefore can not be modified.");
		}

		@Override
		public Optional<String> getLabel() {
			return WVWObjective.this.getLabel();
		}

		@Override
		public IWVWObjectiveType getType() {
			return WVWObjective.this.getType();
		}

		@Override
		public Optional<IWorld> getOwner() {
			final Optional<IWorld> buffer = WVWObjective.this.getOwner();
			return buffer.isPresent() ? Optional.of(buffer.get().createUnmodifiableReference()) : buffer;
		}

		@Override
		public void capture(IWorld capturingWorld) {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName() + " and therefore can not be modified.");
		}

		@Override
		public long getRemainingBuffDuration(TimeUnit unit) {
			return WVWObjective.this.getRemainingBuffDuration(unit);
		}

		@Override
		public Optional<IWVWMap> getMap() {
			final Optional<IWVWMap> buffer = WVWObjective.this.getMap();
			return buffer.isPresent() ? Optional.of(buffer.get().createUnmodifiableReference()) : buffer;
		}

		@Override
		public void connectWithMap(IWVWMap map) {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName() + " and therefore can not be modified.");
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(this).addValue(WVWObjective.this.toString()).toString();
		}

		@Override
		public void updateOnSynchronization() {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName() + " and therefore can not be modified.");
		}

		@Override
		public void initializeOwner(IWorld owningWorld) {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName() + " and therefore can not be modified.");
		}

		@Override
		public Optional<Calendar> getEndOfBuffTimestamp() {
			return WVWObjective.this.getEndOfBuffTimestamp();
		}

		@Override
		public int hashCode() {
			return WVWObjective.this.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return WVWObjective.this.equals(obj);
		}

		@Override
		public Optional<IGuild> getClaimedByGuild() {
			return WVWObjective.this.getClaimedByGuild();
		}

		@Override
		public void claim(IGuild guild) {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName() + " and therefore can not be modified.");
		}

		@Override
		public void initializeClaimedByGuild(IGuild guild) {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName() + " and therefore can not be modified.");
		}
	}

	public static class WVWObjectiveBuilder implements IWVWObjective.IWVWObjectiveBuilder {
		private Optional<IWVWLocationType> location = Optional.absent();
		private Optional<IWorld> owner = Optional.absent();
		private Optional<IWVWMap> map = Optional.absent();
		private Optional<IGuild> claimedByGuild = Optional.absent();

		@Override
		public IWVWObjective build() {
			checkState(this.location.isPresent());
			checkState(this.map != null);

			final IWVWObjective result = new WVWObjective(this.location.get());
			if (this.map.isPresent()) {
				result.connectWithMap(this.map.get());
			}
			result.initializeOwner(this.owner.orNull());
			result.initializeClaimedByGuild(this.claimedByGuild.orNull());
			return result;
		}

		@Override
		public IWVWObjective.IWVWObjectiveBuilder fromDTO(IWVWObjectiveDTO dto) {
			checkNotNull(dto);
			final Optional<IGuildDetailsDTO> guildDetails = dto.getGuildDetails();
			if (guildDetails.isPresent()) {
				final IGuild guild = MODEL_FACTORY.getOrCreateGuild(guildDetails.get().getId(), guildDetails.get().getName(), guildDetails.get().getTag());
				checkState(guild != null);
				this.claimedBy(guild);
			}
			return this.location(WVWLocationType.forObjectiveId(dto.getId()).get());
		}

		@Override
		public IWVWObjective.IWVWObjectiveBuilder location(IWVWLocationType location) {
			this.location = Optional.fromNullable(location);
			return this;
		}

		@Override
		public IWVWObjective.IWVWObjectiveBuilder owner(IWorld world) {
			this.owner = Optional.fromNullable(world);
			return this;
		}

		@Override
		public IWVWObjectiveBuilder map(IWVWMap map) {
			this.map = Optional.fromNullable(map);
			return this;
		}

		@Override
		public IWVWObjectiveBuilder claimedBy(IGuild guild) {
			this.claimedByGuild = Optional.fromNullable(guild);
			return this;
		}
	}

	private final IWVWLocationType location;
	private Optional<IWorld> owningWorld = Optional.absent();
	private Optional<Calendar> lastCaptureEventTimestamp = Optional.absent();
	private boolean postedEndOfBuffEvent = true;
	private Optional<IGuild> claimedByGuild = Optional.absent();
	private Optional<IWVWMap> map = Optional.absent();

	private WVWObjective(IWVWLocationType location) {
		checkNotNull(location);
		checkArgument(location.getObjectiveId().isPresent());
		checkArgument(location.getObjectiveType().isPresent());
		this.location = location;
	}

	@Override
	public Optional<String> getLabel() {
		return this.location.getLabel(YAGW2APIArenanet.INSTANCE.getCurrentLocale());
	}

	@Override
	public IWVWObjectiveType getType() {
		return this.location.getObjectiveType().get();
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("type", this.getType()).add("name", this.getLabel()).add("location", this.location).toString();
	}

	@Override
	public IWVWLocationType getLocation() {
		return this.location;
	}

	@Override
	public void initializeOwner(IWorld owningWorld) {
		checkState(!this.owningWorld.isPresent());
		checkState(!this.lastCaptureEventTimestamp.isPresent());
		checkState(this.postedEndOfBuffEvent);
		this.owningWorld = Optional.fromNullable(owningWorld);
	}

	@Override
	public long getRemainingBuffDuration(TimeUnit unit) {
		if (this.lastCaptureEventTimestamp.isPresent()) {
			final Calendar now = Calendar.getInstance();
			return unit.convert(Math.max(0, this.getType().getBuffDuration(TimeUnit.MILLISECONDS) - Math.max(0, now.getTimeInMillis() - this.lastCaptureEventTimestamp.get().getTimeInMillis())),
					TimeUnit.MILLISECONDS);
		} else {
			// not capture yet
			return 0;
		}
	}

	@Override
	public Optional<IWorld> getOwner() {
		return this.owningWorld;
	}

	@Override
	public Optional<IWVWMap> getMap() {
		return this.map;
	}

	@Override
	public void connectWithMap(IWVWMap map) {
		checkNotNull(map);
		checkState(!this.map.isPresent(), "Connect with map can only be called once.");
		this.map = Optional.of(map);
	}

	@Override
	public IWVWObjective createUnmodifiableReference() {
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
			LOGGER.trace("Remaining buff duration for " + this.toString() + ": " + remainingBuffMillis + "ms");
		}
	}

	@Override
	public Optional<Calendar> getEndOfBuffTimestamp() {
		if (this.lastCaptureEventTimestamp.isPresent()) {
			final Calendar timestamp = Calendar.getInstance();
			timestamp.setTimeInMillis(this.lastCaptureEventTimestamp.get().getTimeInMillis() + this.getType().getBuffDuration(TimeUnit.MILLISECONDS));
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
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof IWVWObjective)) {
			return false;
		} else {
			final IWVWObjective objective = (IWVWObjective) obj;
			return Objects.equal(this.getLocation().getObjectiveId().orNull(), objective.getLocation().getObjectiveId().orNull());
		}
	}

	@Override
	public Optional<IGuild> getClaimedByGuild() {
		return this.claimedByGuild;
	}

	@Override
	public void claim(IGuild guild) {
		if ((guild != null) && (!this.claimedByGuild.isPresent() || !this.claimedByGuild.get().equals(guild))) {
			// changed claiming guild
			final IWVWObjectiveClaimedEvent event = WVW_MODEL_EVENTS_FACTORY.newObjectiveClaimedEvent(this, guild, this.claimedByGuild);
			LOGGER.info(this.getLocation().getLabel(YAGW2APIArenanet.INSTANCE.getCurrentLocale()) + " has been claimed by: " + guild.getName() + " | previous claimed by " + this.claimedByGuild);
			this.claimedByGuild = Optional.of(guild);
			this.getChannel().post(event);
		} else if ((guild == null) && this.claimedByGuild.isPresent()) {
			// unclaim (e.g. after capture)
			final IWVWObjectiveUnclaimedEvent event = WVW_MODEL_EVENTS_FACTORY.newObjectiveUnclaimedEvent(this, this.claimedByGuild);
			LOGGER.info(this.getLocation().getLabel(YAGW2APIArenanet.INSTANCE.getCurrentLocale()) + " has been unclaimed from " + this.claimedByGuild);
			this.claimedByGuild = Optional.absent();
			this.getChannel().post(event);
		} else {
			// no change
			LOGGER.trace(this + " has already been claimed by: " + guild);
		}
	}

	@Override
	public void capture(IWorld capturingWorld) {
		if ((capturingWorld != null) && (!this.owningWorld.isPresent() || !this.owningWorld.get().equals(capturingWorld))) {
			// changed owning world
			final IWVWObjectiveCaptureEvent event = WVW_MODEL_EVENTS_FACTORY.newObjectiveCapturedEvent(this, capturingWorld, this.owningWorld);
			LOGGER.debug(capturingWorld + " has captured " + this + " when expected remaining buff duration was " + this.getRemainingBuffDuration(TimeUnit.SECONDS) + "s");
			this.owningWorld = Optional.of(capturingWorld);
			this.lastCaptureEventTimestamp = Optional.of(event.getTimestamp());
			this.postedEndOfBuffEvent = false;
			this.getChannel().post(event);
		} else {
			// no change
			LOGGER.trace(this + " has already been captured by: " + capturingWorld);
		}
	}

	@Override
	public void initializeClaimedByGuild(IGuild guild) {
		this.claimedByGuild = Optional.fromNullable(guild);
	}

}
