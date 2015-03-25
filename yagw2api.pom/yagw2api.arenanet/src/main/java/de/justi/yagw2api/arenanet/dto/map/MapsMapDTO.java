package de.justi.yagw2api.arenanet.dto.map;

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

import java.util.Collection;

import de.justi.yagwapi.common.Tuple4;

public interface MapsMapDTO {
	String getName();

	String getMinLevel();

	String getMaxLevel();

	String getDefaultFloor();

	Collection<Integer> getFloors();

	String getRegionId();

	String getRegionName();

	String getContinentId();

	String getContinentName();

	/**
	 * @return the dimensions of the map, given as the coordinates of the lower-left (SW) and upper-right (NE) corners.
	 */
	Tuple4<Integer, Integer, Integer, Integer> getBounds();

	/**
	 * 
	 * @return The dimensions of the map within the continent coordinate system, given as the coordinates of the upper-left (NW) and lower-right (SE) corners.
	 */
	Tuple4<Integer, Integer, Integer, Integer> getBoundsOnContinent();

}