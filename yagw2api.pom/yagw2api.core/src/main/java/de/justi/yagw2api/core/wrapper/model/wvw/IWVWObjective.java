package de.justi.yagw2api.core.wrapper.model.wvw;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.IHasChannel;
import de.justi.yagw2api.core.arenanet.dto.IWVWObjectiveDTO;
import de.justi.yagw2api.core.wrapper.model.IGuild;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.types.IWVWLocationType;
import de.justi.yagw2api.core.wrapper.model.wvw.types.IWVWObjectiveType;

public interface IWVWObjective extends IHasWVWLocation<IWVWObjective>, IHasChannel {
	static interface IWVWObjectiveBuilder {
		IWVWObjective build();

		IWVWObjectiveBuilder map(IWVWMap map);
		
		IWVWObjectiveBuilder location(IWVWLocationType location);

		IWVWObjectiveBuilder fromDTO(IWVWObjectiveDTO dto);

		IWVWObjectiveBuilder owner(IWorld world);
		IWVWObjectiveBuilder claimedBy(IGuild guild);
	}

	List<IWVWObjectiveEvent> getEventHistory();

	Optional<String> getLabel();
	Optional<IGuild> getClaimedByGuild();

	IWVWObjectiveType getType();

	Optional<IWorld> getOwner();

	void capture(IWorld capturingWorld);
	void initializeOwner(IWorld owningWorld);
	
	void claim(IGuild guild);
	void initializeClaimedByGuild(IGuild guild);
	
	void updateOnSynchronization();

	long getRemainingBuffDuration(TimeUnit unit);
	Optional<Calendar> getEndOfBuffTimestamp();
	

	Optional<IWVWMap> getMap();
	/**
	 * can only be called once
	 * @param map not null
	 */
	void connectWithMap(IWVWMap map);
}
