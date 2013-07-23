package de.justi.yagw2api.wrapper;


import com.google.common.base.Optional;

public interface IWVWObjectiveCaptureEvent extends IWVWObjectiveEvent {
	IWorld getNewOwningWorld();
	Optional<IWorld> getPreviousOwningWorld();
}
