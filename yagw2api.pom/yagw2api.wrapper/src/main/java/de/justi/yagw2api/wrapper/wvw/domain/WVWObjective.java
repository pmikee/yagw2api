package de.justi.yagw2api.wrapper.wvw.domain;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.dto.wvw.WVWObjectiveDTO;
import de.justi.yagw2api.common.event.HasChannel;
import de.justi.yagw2api.wrapper.guild.domain.Guild;
import de.justi.yagw2api.wrapper.world.domain.World;

public interface WVWObjective extends HasWVWLocation<WVWObjective>, HasChannel {
	static interface WVWObjectiveBuilder {
		WVWObjective build();

		WVWObjectiveBuilder map(WVWMap map);

		WVWObjectiveBuilder location(WVWLocationType location);

		WVWObjectiveBuilder fromDTO(WVWObjectiveDTO dto);

		WVWObjectiveBuilder owner(World world);

		WVWObjectiveBuilder claimedBy(Guild guild);
	}

	Optional<String> getLabel();

	Optional<Guild> getClaimedByGuild();

	WVWObjectiveType getType();

	Optional<World> getOwner();

	void capture(World capturingWorld);

	void initializeOwner(World owningWorld);

	void claim(Guild guild);

	void initializeClaimedByGuild(Guild guild);

	void updateOnSynchronization();

	long getRemainingBuffDuration(TimeUnit unit);

	Optional<LocalDateTime> getEndOfBuffTimestamp();

	Optional<WVWMap> getMap();

	/**
	 * can only be called once
	 *
	 * @param map
	 *            not null
	 */
	void connectWithMap(WVWMap map);
}
