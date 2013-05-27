package de.justi.gw2.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Objects.ToStringHelper;

import de.justi.gw2.model.IWorld;
import de.justi.gw2.model.wvw.IWVWObjective;
import de.justi.gw2.model.wvw.events.IWVWObjectiveCaptureEvent;


class WVWObjectiveCaptureEvent extends AbstractWVWObjectiveEvent implements IWVWObjectiveCaptureEvent {

	private final IWorld newOwningWorld;
	private final Optional<IWorld> previousOwningWorld;
	
	public WVWObjectiveCaptureEvent(IWVWObjective source, IWorld newOwningWorld) {
		this(checkNotNull(source), checkNotNull(newOwningWorld), null);
	}
	
	public WVWObjectiveCaptureEvent(IWVWObjective source, IWorld newOwningWorld, IWorld previousOwningWorld) {
		super(checkNotNull(source));
		this.newOwningWorld = checkNotNull(newOwningWorld);
		this.previousOwningWorld = Optional.fromNullable(previousOwningWorld);		
	}

	public IWorld getNewOwningWorld() {
		return this.newOwningWorld;
	}

	public Optional<IWorld> getPreviousOwningWorld() {
		return this.previousOwningWorld;
	}
	
	public String toString() {
		final ToStringHelper helper = Objects.toStringHelper(this).add("super", super.toString());
		helper.add("objective", this.getSource());
		if(this.getSource().getMap().isPresent()) {
			helper.add("mapType",this.getSource().getMap().get().getType());
			if(this.getSource().getMap().get().getMatch().isPresent()) {
				helper.add("matchId",this.getSource().getMap().get().getMatch().get().getId());
			}
		}
		helper.add("capturedBy", this.newOwningWorld);
		if(this.previousOwningWorld.isPresent()) {
			helper.add("from", this.previousOwningWorld.get());
		}
		return helper.toString();
	}
}