package de.justi.gw2.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;

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
}
