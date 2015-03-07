package de.justi.yagw2api.wrapper;

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

import de.justi.yagw2api.arenanet.IWVWMatchDTO;
import de.justi.yagwapi.common.IHasChannel;

public interface IWVWMatch extends IHasChannel {
	interface IWVWMatchBuilder {
		IWVWMatch build();

		IWVWMatchBuilder fromMatchDTO(IWVWMatchDTO dto, Locale locale);

		IWVWMatchBuilder redScore(int score);

		IWVWMatchBuilder blueScore(int score);

		IWVWMatchBuilder greenScore(int score);

		IWVWMatchBuilder start(LocalDateTime date);

		IWVWMatchBuilder end(LocalDateTime date);
	}

	Set<IWorld> searchWorldsByNamePattern(Pattern searchPattern);

	String getId();

	IWorld[] getWorlds();

	IWorld getRedWorld();

	IWorld getGreenWorld();

	IWorld getBlueWorld();

	Optional<IWorld> getWorldByDTOOwnerString(String dtoOwnerString);

	IWVWMap getCenterMap();

	IWVWMap getBlueMap();

	IWVWMap getRedMap();

	IWVWMap getGreenMap();

	IWVWScores getScores();

	LocalDateTime getStartTimestamp();

	LocalDateTime getEndTimestamp();

	int calculateGreenTick();

	int calculateBlueTick();

	int calculateRedTick();

	IWVWMatch createUnmodifiableReference();
}
