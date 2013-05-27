package de.justi.gw2.model.wvw.events;


import com.google.common.base.Optional;

import de.justi.gw2.model.IWorld;

public interface IWVWObjectiveCaptureEvent extends IWVWObjectiveEvent {
	IWorld getNewOwningWorld();
	Optional<IWorld> getPreviousOwningWorld();
}
