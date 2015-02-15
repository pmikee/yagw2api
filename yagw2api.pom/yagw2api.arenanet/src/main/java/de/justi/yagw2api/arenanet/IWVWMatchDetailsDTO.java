package de.justi.yagw2api.arenanet;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import com.google.common.base.Optional;

public interface IWVWMatchDetailsDTO {

	String getMatchID();

	int getRedScore();

	int getGreenScore();

	int getBlueScore();

	IWVWMapDTO[] getMaps();

	Optional<IWVWMapDTO> getMapForTypeString(String dtoMapTypeString);

	IWVWMapDTO getCenterMap();

	IWVWMapDTO getRedMap();

	IWVWMapDTO getGreenMap();

	IWVWMapDTO getBlueMap();

	Optional<IWVWMatchDTO> getMatch();
}
