package de.justi.yagw2api.wrapper.map.domain.impl;

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

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.v1.MapFloorService;
import de.justi.yagw2api.arenanet.v1.MapService;
import de.justi.yagw2api.arenanet.v1.MapTileService;
import de.justi.yagw2api.wrapper.map.domain.Continent.ContinentBuilder;
import de.justi.yagw2api.wrapper.map.domain.ContinentFloor.ContinentFloorBuilder;
import de.justi.yagw2api.wrapper.map.domain.Map.MapBuilder;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;
import de.justi.yagw2api.wrapper.map.domain.MapTile.MapTileBuilder;
import de.justi.yagw2api.wrapper.map.domain.POILandmark.POILandmarkBuilder;
import de.justi.yagw2api.wrapper.map.domain.POIUnlock.POIUnlockBuilder;
import de.justi.yagw2api.wrapper.map.domain.POIVista.POIVistaBuilder;
import de.justi.yagw2api.wrapper.map.domain.POIWaypoint.POIWaypointBuilder;
import de.justi.yagw2api.wrapper.map.event.MapEventFactory;

public final class DefaultMapDomainFactory implements MapDomainFactory {
	// FIELDS
	private final EventBus eventbus;
	private final MapService mapService;
	private final MapFloorService mapFloorService;
	private final MapTileService mapTileService;
	private final MapEventFactory mapEventFactory;

	// CONSTRUCTOR
	@Inject
	public DefaultMapDomainFactory(final EventBus eventbus, final MapFloorService mapFloorService, final MapTileService mapTileService, final MapEventFactory mapEventFactory,
			final MapService mapService) {
		this.eventbus = checkNotNull(eventbus, "missing eventbus");
		this.mapService = checkNotNull(mapService, "missing mapService");
		this.mapFloorService = checkNotNull(mapFloorService, "missing mapFloorService");
		this.mapTileService = checkNotNull(mapTileService, "missing mapTileService");
		this.mapEventFactory = checkNotNull(mapEventFactory, "missing mapEventFactory");
	}

	// METHODS
	@Override
	public ContinentBuilder newContinentBuilder() {
		return DefaultContinent.builder(this, this.mapService, this.mapFloorService);
	}

	@Override
	public ContinentFloorBuilder newContinentFloorBuilder() {
		return DefaultContinentFloor.builder(this.mapFloorService, this.mapService, this);
	}

	@Override
	public MapTileBuilder newMapTileBuilder() {
		return DefaultMapTile.builder(this.eventbus, this.mapTileService, this.mapEventFactory);
	}

	@Override
	public MapTileBuilder newMapUnavailableTileBuilder() {
		return DefaultUnavailableMapTile.builder();
	}

	@Override
	public MapBuilder newMapBuilder() {
		return DefaultMap.builder();
	}

	@Override
	public POILandmarkBuilder newPOILandmarkBuilder() {
		return DefaultPOILandmark.builder();
	}

	@Override
	public POIVistaBuilder newPOIVistaBuilder() {
		return DefaultPOIVista.builder();
	}

	@Override
	public POIWaypointBuilder newPOIWaypointBuilder() {
		return DefaultPOIWaypoint.builder();
	}

	@Override
	public POIUnlockBuilder newPOIUnlockBuilder() {
		return DefaultPOIUnlock.builder();
	}

}
