package de.justi.yagw2api.wrapper.map.event.impl;

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

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagw2api.wrapper.map.event.MapEventFactory;
import de.justi.yagw2api.wrapper.map.event.MapTileImageFailedToLoadEvent;
import de.justi.yagw2api.wrapper.map.event.MapTileImageLoadedSuccessfullyEvent;
import de.justi.yagw2api.wrapper.map.event.MapTileImageNotAvailableEvent;

public final class DefaultMapEventFactory implements MapEventFactory {

	@Override
	public MapTileImageFailedToLoadEvent newMapTileImageFailedToLoad(final MapTile tile) {
		return new DefaultMapTileImageFailedToLoadEvent(checkNotNull(tile, "missing tile"));
	}

	@Override
	public MapTileImageLoadedSuccessfullyEvent newMapTileImageLoadedSuccessfully(final MapTile tile) {
		return new DefaultMapTileImageLoadedSuccessfullyEvent(checkNotNull(tile, "missing tile"));
	}

	@Override
	public MapTileImageNotAvailableEvent newMapTileImageNotAvailable(final MapTile tile) {
		return new DefaultMapTileImageNotAvailableEvent(checkNotNull(tile, "missing tile"));
	}

}
