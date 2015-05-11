package de.justi.yagw2api.wrapper.map.domain;

import java.nio.file.Path;

import javax.annotation.Nullable;

import de.justi.yagwapi.common.Tuple2;

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

public interface MapTile {

	public static interface MapTileBuilder {
		MapTileBuilder position(@Nullable Tuple2<Integer, Integer> position);

		MapTileBuilder continentId(@Nullable String continentId);

		MapTileBuilder zoom(int zoom);

		MapTileBuilder floorIndex(int floorIndex);

		MapTile build();
	}

	Path getImagePath();

}
