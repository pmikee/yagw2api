package de.justi.gw2.model.wvw;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;

import de.justi.gw2.api.dto.IWVWObjectiveDTO;
import de.justi.gw2.model.IHasChannel;
import de.justi.gw2.model.IWorld;
import de.justi.gw2.model.wvw.events.IWVWObjectiveEvent;
import de.justi.gw2.model.wvw.types.IWVWLocationType;
import de.justi.gw2.model.wvw.types.IWVWObjectiveType;

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

	long getRemainingBuffDuration(TimeUnit unit);
	

	Optional<IWVWMap> getMap();
	/**
	 * can only be called once
	 * @param map not null
	 */
	void connectWithMap(IWVWMap map);
}
