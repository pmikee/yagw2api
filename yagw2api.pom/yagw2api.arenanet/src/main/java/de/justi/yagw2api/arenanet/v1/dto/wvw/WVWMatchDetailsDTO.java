package de.justi.yagw2api.arenanet.v1.dto.wvw;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
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

import com.google.common.base.Optional;

public interface WVWMatchDetailsDTO {

	String getMatchID();

	int getRedScore();

	int getGreenScore();

	int getBlueScore();

	WVWMapDTO[] getMaps();

	Optional<WVWMapDTO> getMapForTypeString(String dtoMapTypeString);

	WVWMapDTO getCenterMap();

	WVWMapDTO getRedMap();

	WVWMapDTO getGreenMap();

	WVWMapDTO getBlueMap();

	Optional<WVWMatchDTO> getMatch();
}
