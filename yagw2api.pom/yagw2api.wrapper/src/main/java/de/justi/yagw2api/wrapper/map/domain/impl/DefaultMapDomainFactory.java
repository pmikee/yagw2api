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

import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.MapFloorService;
import de.justi.yagw2api.wrapper.map.domain.Continent.ContinentBuilder;
import de.justi.yagw2api.wrapper.map.domain.ContinentMap.ContinentMapBuilder;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;
import de.justi.yagw2api.wrapper.map.domain.MapFloor.MapFloorBuilder;

public final class DefaultMapDomainFactory implements MapDomainFactory {
	// FIELDS
	private final MapFloorService mapFloorService;

	// CONSTRUCTOR
	@Inject
	public DefaultMapDomainFactory(final MapFloorService mapFloorService) {
		this.mapFloorService = checkNotNull(mapFloorService, "missing mapFloorService");
	}

	// METHODS
	@Override
	public ContinentBuilder newContinentBuilder() {
		return DefaultContinent.builder();
	}

	@Override
	public ContinentMapBuilder newContinentMapBuilder() {
		return DefaultContinentMap.builder(this, this.mapFloorService);
	}

	@Override
	public MapFloorBuilder newMapFloorBuilder() {
		return DefaultMapFloor.builder();
	}

}
