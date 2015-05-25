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

import java.util.Optional;

import javax.annotation.Nullable;

import de.justi.yagwapi.common.Tuple2;
import de.justi.yagwapi.common.Tuple4;

public interface MapFloor {

	public static interface MapFloorTilesBuilder {
		MapFloorTilesBuilder continentId(@Nullable String continentId);

		MapFloorTilesBuilder floorIndex(int floorIndex);

		MapFloorTilesBuilder minZoom(int minZoom);

		MapFloorTilesBuilder maxZoom(int maxZoom);

		MapFloor build();
	}

	int getIndex();

	Optional<MapTile> getTileUnchecked(int x, int y, int zoom);

	MapTile getTile(int x, int y, int zoom) throws NoSuchMapTileException;

	int getTileTextureSize(int zoom);

	Tuple2<Integer, Integer> getTextureDimension();

	Tuple2<Integer, Integer> getTileIndexDimension(final int zoom);

	Tuple4<Integer, Integer, Integer, Integer> getClampedTextureDimension();

	Tuple4<Integer, Integer, Integer, Integer> getClampedTileIndexDimension(final int zoom);
}
