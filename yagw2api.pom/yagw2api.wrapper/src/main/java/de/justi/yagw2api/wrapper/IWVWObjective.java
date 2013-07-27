package de.justi.yagw2api.wrapper;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.IWVWObjectiveDTO;
import de.justi.yagwapi.common.IHasChannel;

public interface IWVWObjective extends IHasWVWLocation<IWVWObjective>, IHasChannel {
	static interface IWVWObjectiveBuilder {
		IWVWObjective build();

		IWVWObjectiveBuilder map(IWVWMap map);

		IWVWObjectiveBuilder location(IWVWLocationType location);

		IWVWObjectiveBuilder fromDTO(IWVWObjectiveDTO dto);

		IWVWObjectiveBuilder owner(IWorld world);

		IWVWObjectiveBuilder claimedBy(IGuild guild);
	}

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
	 * 
	 * @param map
	 *            not null
	 */
	void connectWithMap(IWVWMap map);
}