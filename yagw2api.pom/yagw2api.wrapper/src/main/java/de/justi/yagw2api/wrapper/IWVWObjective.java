package de.justi.yagw2api.wrapper;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import java.time.LocalDateTime;
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

	Optional<LocalDateTime> getEndOfBuffTimestamp();

	Optional<IWVWMap> getMap();

	/**
	 * can only be called once
	 * 
	 * @param map
	 *            not null
	 */
	void connectWithMap(IWVWMap map);
}
