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
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.dto.wvw.WVWMatchDTO;
import de.justi.yagw2api.wrapper.world.domain.World;
import de.justi.yagwapi.common.event.HasChannel;

public interface WVWMatch extends HasChannel {
	interface WVWMatchBuilder {
		WVWMatch build();

		WVWMatchBuilder fromMatchDTO(WVWMatchDTO dto, Locale locale);

		WVWMatchBuilder redScore(int score);

		WVWMatchBuilder blueScore(int score);

		WVWMatchBuilder greenScore(int score);

		WVWMatchBuilder start(LocalDateTime date);

		WVWMatchBuilder end(LocalDateTime date);
	}

	Set<World> searchWorldsByNamePattern(Pattern searchPattern);

	String getId();

	World[] getWorlds();

	World getRedWorld();

	World getGreenWorld();

	World getBlueWorld();

	Optional<World> getWorldByDTOOwnerString(String dtoOwnerString);

	WVWMap getCenterMap();

	WVWMap getBlueMap();

	WVWMap getRedMap();

	WVWMap getGreenMap();

	WVWScores getScores();

	LocalDateTime getStartTimestamp();

	LocalDateTime getEndTimestamp();

	int calculateGreenTick();

	int calculateBlueTick();

	int calculateRedTick();

	WVWMatch createUnmodifiableReference();
}
