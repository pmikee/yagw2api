package model.wvw;

import java.util.List;
import java.util.concurrent.TimeUnit;

import model.IHasChannel;
import model.IWorld;


public interface IWVWObjective extends IHasWVWLocation, IHasChannel {
	List<IWVWObjectiveEvent> getEventHistory();
	String getLabel();
	IWVWObjectiveType getType();
	void capture(IWorld capturingWorld);
	long getRemainingBuffDuration(TimeUnit unit);
}
