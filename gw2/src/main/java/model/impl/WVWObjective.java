package model.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import model.IWVWLocationType;
import model.IWVWObjective;
import model.IWVWObjectiveEvent;
import model.IWVWObjectiveType;
import model.IWorld;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

class WVWObjective extends AbstractHasChannel implements IWVWObjective{
	private final IWVWLocationType location;
	private final List<IWVWObjectiveEvent> eventHistory = new CopyOnWriteArrayList<IWVWObjectiveEvent>();
	private Optional<IWorld> owningWorld = Optional.absent();
	private Optional<Calendar> lastCaptureEventTimestamp = Optional.absent();

	public WVWObjective(IWVWLocationType location){
		checkNotNull(location);
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
		final WVWObjectiveCaptureEvent event = new WVWObjectiveCaptureEvent(this, capturingWorld, this.owningWorld.orNull()); 
		this.owningWorld = Optional.of(capturingWorld);
		this.lastCaptureEventTimestamp = Optional.of(event.getTimestamp());
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
}
