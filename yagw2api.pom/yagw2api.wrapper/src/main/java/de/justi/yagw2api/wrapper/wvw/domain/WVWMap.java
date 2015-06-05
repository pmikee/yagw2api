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

import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.dto.wvw.WVWMapDTO;
import de.justi.yagw2api.common.event.HasChannel;

public interface WVWMap extends HasChannel {
	static interface WVWMapBuilder {
		WVWMap build();

		WVWMapBuilder type(WVWMapType type);

		WVWMapBuilder objective(WVWObjective objective);

		WVWMapBuilder fromDTO(WVWMapDTO dto);

		WVWMapBuilder match(WVWMatch match);

		WVWMapBuilder redScore(int score);

		WVWMapBuilder blueScore(int score);

		WVWMapBuilder greenScore(int score);
	}

	Optional<WVWMatch> getMatch();

	WVWMapType getType();

	Map<WVWLocationType, HasWVWLocation<?>> getMappedByPosition();

	Set<HasWVWLocation<?>> getEverything();

	Set<WVWObjective> getObjectives();

	Optional<WVWObjective> getByObjectiveId(int id);

	Optional<HasWVWLocation<?>> getByLocation(WVWLocationType location);

	WVWScores getScores();

	int calculateGreenTick();

	int calculateBlueTick();

	int calculateRedTick();

	WVWMap createUnmodifiableReference();

	/**
	 * can only be called once
	 *
	 * @param map
	 *            not null
	 */
	void connectWithMatch(WVWMatch match);
}
