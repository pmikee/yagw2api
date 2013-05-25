package model;

import com.google.common.base.Optional;

public interface IWVWObjectiveCaptureEvent extends IWVWObjectiveEvent {
	IWorld getNewOwningWorld();
	Optional<IWorld> getPreviousOwningWorld();
}
