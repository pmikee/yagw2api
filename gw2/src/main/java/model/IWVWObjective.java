package model;

import java.util.List;
import java.util.concurrent.TimeUnit;


public interface IWVWObjective extends IHasWVWLocation, IHasChannel {
	List<IWVWObjectiveEvent> getEventHistory();
	String getLabel();
	IWVWObjectiveType getType();
	void capture(IWorld capturingWorld);
	long getRemainingBuffDuration(TimeUnit unit);
}
