package de.justi.yagw2api.wrapper.map.domain;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import javax.annotation.Nullable;

import de.justi.yagwapi.common.tuple.UniformNumberTuple2;

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

		MapTileBuilder position(@Nullable UniformNumberTuple2<Integer> position);

		MapTileBuilder continentId(@Nullable String continentId);

		MapTileBuilder zoom(int zoom);

		MapTileBuilder floorIndex(int floorIndex);

		MapTile build();
	}

	UniformNumberTuple2<Integer> getPosition();

	int getFloorIndex();

	int getZoom();

	String getContinentId();

	/**
	 * Starts downloading the tile image from the gw2 servers if not done yet.<br />
	 * <strong>Does not block.</strong><br />
	 *
	 * @see MapTile#getImagePath(boolean) MapTile.getImagePath(true) for a blocking version
	 *
	 * @return a placeholder image while downloading, the tile image path cached in the local {@link FileSystem} otherwise
	 */
	Path getImagePath();

	/**
	 *
	 * Starts downloading the tile image from the gw2 servers if not done yet.<br />
	 * <strong>May block or not, depending on {@code blockUntilLoaded} parameter.</strong><br />
	 *
	 * @param blockUntilLoaded
	 *            control whether this method should block until the tile image has been loaded
	 * @return a placeholder image while downloading, the tile image path cached in the local {@link FileSystem} otherwise
	 */
	Path getImagePath(boolean blockUntilLoaded);
}
