package de.justi.yagw2api.wrapper.model.wvw;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;

import de.justi.yagw2api.api.dto.IWVWObjectiveDTO;
import de.justi.yagw2api.wrapper.model.IHasChannel;
import de.justi.yagw2api.wrapper.model.IWorld;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWObjectiveEvent;
import de.justi.yagw2api.wrapper.model.wvw.types.IWVWLocationType;
import de.justi.yagw2api.wrapper.model.wvw.types.IWVWObjectiveType;

public interface IWVWObjective extends IHasWVWLocation<IWVWObjective>, IHasChannel {
	static interface IWVWObjectiveBuilder {
		IWVWObjective build();

		IWVWObjectiveBuilder map(IWVWMap map);
		
		IWVWObjectiveBuilder location(IWVWLocationType location);

		IWVWObjectiveBuilder fromDTO(IWVWObjectiveDTO dto);

		IWVWObjectiveBuilder owner(IWorld world);
	}

	List<IWVWObjectiveEvent> getEventHistory();

	String getLabel();

	IWVWObjectiveType getType();

	Optional<IWorld> getOwner();

	void capture(IWorld capturingWorld);
	void updateOnSynchronization();

	long getRemainingBuffDuration(TimeUnit unit);
	

	Optional<IWVWMap> getMap();
	/**
	 * can only be called once
	 * @param map not null
	 */
	void connectWithMap(IWVWMap map);
}