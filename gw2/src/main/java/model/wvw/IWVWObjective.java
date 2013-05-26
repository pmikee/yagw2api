package model.wvw;

import java.util.List;
import java.util.concurrent.TimeUnit;

import api.dto.IWVWObjectiveDTO;

import model.IHasChannel;
import model.IWorld;


public interface IWVWObjective extends IHasWVWLocation, IHasChannel {
	static interface IWVWObjectiveBuilder {
		IWVWObjective build();
		IWVWObjectiveBuilder location(IWVWLocationType location);
		IWVWObjectiveBuilder fromDTO(IWVWObjectiveDTO dto);
		IWVWObjectiveBuilder owner(IWorld world);
	}
	
	List<IWVWObjectiveEvent> getEventHistory();
	String getLabel();
	IWVWObjectiveType getType();
	void capture(IWorld capturingWorld);
	long getRemainingBuffDuration(TimeUnit unit);
}
