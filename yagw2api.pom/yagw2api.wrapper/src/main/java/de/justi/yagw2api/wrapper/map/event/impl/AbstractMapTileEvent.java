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

import com.google.common.base.MoreObjects.ToStringHelper;

import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagw2api.wrapper.map.event.MapTileEvent;
import de.justi.yagwapi.common.AbstractEvent;

abstract class AbstractMapTileEvent extends AbstractEvent implements MapTileEvent {
	private final MapTile mapTile;

	protected AbstractMapTileEvent(final MapTile mapTile) {
		this.mapTile = checkNotNull(mapTile, "missing mapTile");
	}

	/**
	 * @return the mapTile
	 */
	@Override
	public final MapTile getMapTile() {
		return this.mapTile;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("mapTile", this.mapTile);
	}

}
