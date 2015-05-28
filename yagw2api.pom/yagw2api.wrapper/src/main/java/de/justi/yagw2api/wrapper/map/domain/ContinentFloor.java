package de.justi.yagw2api.wrapper.map.domain;

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

import java.util.NavigableSet;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;

import de.justi.yagw2api.arenanet.dto.map.MapDTO;
import de.justi.yagwapi.common.tuple.NumberTuple2;
import de.justi.yagwapi.common.tuple.NumberTuple4;

public interface ContinentFloor {

	public static interface ContinentFloorBuilder {
		ContinentFloorBuilder mapDTOLoader(Function<String, Optional<? extends MapDTO>> mapDTOLoader);

		ContinentFloorBuilder continentId(@Nullable String continentId);

		ContinentFloorBuilder mapIds(final Supplier<NavigableSet<String>> mapIds);

		ContinentFloorBuilder floorIndex(int floorIndex);

		ContinentFloorBuilder minZoom(int minZoom);

		ContinentFloorBuilder maxZoom(int maxZoom);

		ContinentFloor build();
	}

	int getIndex();

	NavigableSet<String> getMapIds();

	Optional<Map> getMap(String mapId);

	/**
	 *
	 * @return an {@link Iterable} of the most significant maps of this floor. A map is most significant if it's the first map (ordered by id) for it's bounds.
	 */
	Iterable<Map> getMostSignificantMaps();

	Iterable<Map> getMaps();

	MapTile getTile(int x, int y, int zoom) throws NoSuchMapTileException;

	NumberTuple2<Integer, Integer> getTextureDimension();

	NumberTuple4<Integer, Integer, Integer, Integer> getClampedTextureDimension();

	NumberTuple4<Integer, Integer, Integer, Integer> getClampedTileIndexDimension(final int zoom);
}
