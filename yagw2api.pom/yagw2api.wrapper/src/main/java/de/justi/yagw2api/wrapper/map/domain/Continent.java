package de.justi.yagw2api.wrapper.map.domain;

import java.util.Set;
import java.util.SortedSet;

import javax.annotation.Nullable;

import com.google.common.base.Optional;

import de.justi.yagw2api.common.tuple.IntTuple2;

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

public interface Continent {

	static interface ContinentBuilder {
		Continent build();

		ContinentBuilder mapIds(final Set<String> mapIds);

		ContinentBuilder floorIndices(final Set<String> floorIndices);

		ContinentBuilder name(@Nullable String name);

		ContinentBuilder id(@Nullable String id);

		ContinentBuilder dimension(@Nullable IntTuple2 dimension);

		ContinentBuilder minZoom(int zoom);

		ContinentBuilder maxZoom(int zoom);
	}

	int getMinZoom();

	int getMaxZoom();

	String getId();

	IntTuple2 getDimension();

	String getName();

	Optional<ContinentFloor> findFloor(String floorIndex);

	ContinentFloor getFloor(String floorIndex) throws NoSuchContinentFloorException;

	Iterable<ContinentFloor> getFloors();

	SortedSet<String> getFloorIdices();

	Optional<Map> getMap(String mapId);

	Iterable<Map> getMaps();

	int getTileTextureSize(int zoom);
}
