package de.justi.yagw2api.core.wrapper.model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;

import de.justi.yagw2api.core.YAGW2APICore;
import de.justi.yagw2api.core.arenanet.dto.IWVWObjectiveDTO;
import de.justi.yagw2api.core.wrapper.model.AbstractHasChannel;
import de.justi.yagw2api.core.wrapper.model.IUnmodifiable;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWModelEventFactory;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.types.IWVWLocationType;
import de.justi.yagw2api.core.wrapper.model.wvw.types.IWVWObjectiveType;
import de.justi.yagw2api.core.wrapper.model.wvw.types.WVWLocationType;

class WVWObjective extends AbstractHasChannel implements IWVWObjective {
	private static final Logger LOGGER = Logger.getLogger(WVWObjective.class);
	private static final IWVWModelEventFactory WVW_MODEL_EVENTS_FACTORY = YAGW2APICore.getInjector().getInstance(IWVWModelEventFactory.class);

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
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName()
					+ " and therefore can not be modified.");
		}

		@Override
		public List<IWVWObjectiveEvent> getEventHistory() {
			return Collections.unmodifiableList(WVWObjective.this.getEventHistory());
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
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName()
					+ " and therefore can not be modified.");
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
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName()
					+ " and therefore can not be modified.");
		}

		public String toString() {
			return Objects.toStringHelper(this).addValue(WVWObjective.this.toString()).toString();
		}

		@Override
		public void updateOnSynchronization() {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName()
					+ " and therefore can not be modified.");
		}

		@Override
		public void initializeOwner(IWorld owningWorld) {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName()
					+ " and therefore can not be modified.");			
		}

		@Override
		public Optional<Calendar> getEndOfBuffTimestamp() {
			return WVWObjective.this.getEndOfBuffTimestamp();
		}
	}

	public static class WVWObjectiveBuilder implements IWVWObjective.IWVWObjectiveBuilder {
		private Optional<IWVWLocationType> location = Optional.absent();
		private Optional<IWorld> owner = Optional.absent();
		private Optional<IWVWMap> map = Optional.absent();

		@Override
		public IWVWObjective build() {
			checkState(this.location.isPresent());

			final IWVWObjective result = new WVWObjective(this.location.get());
			if (this.map.isPresent()) {
				result.connectWithMap(map.get());
			}
			if (this.owner.isPresent()) {
				result.initializeOwner(this.owner.get());
			}
			return result;
		}

		@Override
		public IWVWObjective.IWVWObjectiveBuilder fromDTO(IWVWObjectiveDTO dto) {
			checkNotNull(dto);
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

	}

	private final IWVWLocationType location;
	private final List<IWVWObjectiveEvent> eventHistory = new CopyOnWriteArrayList<IWVWObjectiveEvent>();
	private Optional<IWorld> owningWorld = Optional.absent();
	private Optional<Calendar> lastCaptureEventTimestamp = Optional.absent();
	private boolean postedEndOfBuffEvent = true;
	private Optional<IWVWMap> map = Optional.absent();

	private WVWObjective(IWVWLocationType location) {
		checkNotNull(location);
		checkArgument(location.getObjectiveId().isPresent());
		checkArgument(location.getObjectiveType().isPresent());
		this.location = location;
	}

	public Optional<String> getLabel() {
		return this.location.getLabel(Locale.getDefault());
	}

	public IWVWObjectiveType getType() {
		return this.location.getObjectiveType().get();
	}

	public String toString() {
		return Objects.toStringHelper(this).add("type", this.getType()).add("name", this.getLabel()).add("location", this.location).toString();
	}

	public IWVWLocationType getLocation() {
		return this.location;
	}

	public List<IWVWObjectiveEvent> getEventHistory() {
		return ImmutableList.copyOf(this.eventHistory);
	}

	public void capture(IWorld capturingWorld) {
		checkNotNull(capturingWorld);
		final IWVWObjectiveCaptureEvent event = WVW_MODEL_EVENTS_FACTORY.newObjectiveCapturedEvent(this, capturingWorld, this.owningWorld);
		LOGGER.debug(capturingWorld + " has captured " + this + " when expected remaining buff duration was " + this.getRemainingBuffDuration(TimeUnit.SECONDS)
				+ "s");
		this.owningWorld = Optional.of(capturingWorld);
		this.lastCaptureEventTimestamp = Optional.of(event.getTimestamp());
		this.postedEndOfBuffEvent = false;
		this.getChannel().post(event);
	}

	@Override
	public void initializeOwner(IWorld owningWorld) {
		checkNotNull(owningWorld);
		checkState(!this.owningWorld.isPresent());
		checkState(!this.lastCaptureEventTimestamp.isPresent());
		checkState(this.postedEndOfBuffEvent);
		this.owningWorld = Optional.of(owningWorld);
	}

	public long getRemainingBuffDuration(TimeUnit unit) {
		if (this.lastCaptureEventTimestamp.isPresent()) {
			final Calendar now = Calendar.getInstance();
			return unit.convert(
					Math.max(
							0,
							this.getType().getBuffDuration(TimeUnit.MILLISECONDS)
									- Math.max(0, now.getTimeInMillis() - this.lastCaptureEventTimestamp.get().getTimeInMillis())), TimeUnit.MILLISECONDS);
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
		if (!this.postedEndOfBuffEvent && remainingBuffMillis == 0) {
			synchronized (this) {
				if (!this.postedEndOfBuffEvent) {
					this.postedEndOfBuffEvent = true;
					this.getChannel().post(WVW_MODEL_EVENTS_FACTORY.newObjectiveEndOfBuffEvent(this));
				}
			}
		} else if (remainingBuffMillis > 0 && LOGGER.isTraceEnabled()) {
			LOGGER.trace("Remaining buff duration for " + this.toString() + ": " + remainingBuffMillis + "ms");
		}
	}
	@Override
	public Optional<Calendar> getEndOfBuffTimestamp() {
		if(this.lastCaptureEventTimestamp.isPresent()) {
			final Calendar timestamp = Calendar.getInstance();
			timestamp.setTimeInMillis(this.lastCaptureEventTimestamp.get().getTimeInMillis()+this.getType().getBuffDuration(TimeUnit.MILLISECONDS));
			return Optional.of(timestamp);
		}else {
			return Optional.absent();
		}
	}
}
