package de.justi.yagw2api.wrapper.model.wvw.events;


import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.model.IWorld;

public interface IWVWObjectiveCaptureEvent extends IWVWObjectiveEvent {
	IWorld getNewOwningWorld();
	Optional<IWorld> getPreviousOwningWorld();
}
