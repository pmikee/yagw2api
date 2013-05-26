package model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import model.AbstractHasChannel;
import model.IModelFactory;
import model.IWorld;
import model.wvw.IWVWModelFactory;
import model.wvw.events.IWVWModelEventFactory;
import model.wvw.events.IWVWObjectiveCaptureEvent;
import model.wvw.events.IWVWObjectiveEvent;
import model.wvw.types.IWVWLocationType;
import model.wvw.types.IWVWObjective;
import model.wvw.types.IWVWObjectiveType;
import model.wvw.types.impl.WVWLocationType;

import org.apache.log4j.Logger;

import utils.InjectionHelper;
import api.dto.IWVWObjectiveDTO;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

class WVWObjective extends AbstractHasChannel implements IWVWObjective{
	private static final Logger LOGGER = Logger.getLogger(WVWObjective.class);
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);
	private static final IWVWModelEventFactory WVW_MODEL_EVENTS_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelEventFactory.class);
	private static final IModelFactory MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IModelFactory.class);
	
	public static class WVWObjectiveBuilder implements IWVWObjective.IWVWObjectiveBuilder {		
		private Optional<IWVWLocationType> location = Optional.absent();
		private Optional<IWorld> owner = Optional.absent();
		
		@Override
		public IWVWObjective build() {
			final IWVWObjective result = new WVWObjective(this.location.get());
			if(this.owner.isPresent()) {
				result.capture(this.owner.get());
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

	}

	
	private final IWVWLocationType location;
	private final List<IWVWObjectiveEvent> eventHistory = new CopyOnWriteArrayList<IWVWObjectiveEvent>();
	private Optional<IWorld> owningWorld = Optional.absent();
	private Optional<Calendar> lastCaptureEventTimestamp = Optional.absent();

	private WVWObjective(IWVWLocationType location){
		checkNotNull(location);
		checkArgument(location.getObjectiveId().isPresent());
		checkArgument(location.getObjectiveType().isPresent());
		this.location = location;
	}
	
	public String getLabel() {
		return this.location.getLabel();
	}

	public IWVWObjectiveType getType() {
		return this.location.getObjectiveType().get();
	}
	
	public String toString() {
		return Objects.toStringHelper(this).add("type", this.getType()).add("name", this.getLabel()).add("location",this.location).toString();
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
		this.owningWorld = Optional.of(capturingWorld);
		this.lastCaptureEventTimestamp = Optional.of(event.getTimestamp());
		LOGGER.debug(capturingWorld+" has captured "+this);
		this.getChannel().post(event);
	}

	public long getRemainingBuffDuration(TimeUnit unit) {
		if(this.lastCaptureEventTimestamp.isPresent()) {
			final Calendar now = Calendar.getInstance();
			return unit.convert(Math.max(0,now.getTimeInMillis()-this.lastCaptureEventTimestamp.get().getTimeInMillis()), TimeUnit.MILLISECONDS);
		}else {
			// not capture yet
			return 0;
		}
	}

	@Override
	public Optional<IWorld> getOwner() {
		return this.owningWorld;
	}
}
