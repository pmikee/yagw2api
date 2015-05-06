package de.justi.yagw2api.wrapper.map;

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

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.MapContinentService;
import de.justi.yagw2api.arenanet.dto.map.MapContinentWithIdDTO;
import de.justi.yagw2api.wrapper.map.domain.Continent;
import de.justi.yagw2api.wrapper.map.domain.ContinentMap;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;

public final class DefaultMapWrapper implements MapWrapper {

	// CONSTS

	// EMBEDDED

	// FIELDS
	private final MapDomainFactory mapDomainFactory;
	private final MapContinentService mapContinentService;
	private final Function<MapContinentWithIdDTO, Continent> continentConverter;

	// CONSTRUCTOR
	@Inject
	public DefaultMapWrapper(final MapDomainFactory mapDomainFactory, final MapContinentService mapContinentService) {
		this.mapContinentService = checkNotNull(mapContinentService, "missing mapContinentService");
		this.mapDomainFactory = checkNotNull(mapDomainFactory, "missing mapDomainFactory");
		this.continentConverter = input -> {
			checkNotNull(input, "missing input");
			//@formatter:off
			final ContinentMap map = mapDomainFactory.newContinentMapBuilder().
					continentId(input.getId()).
					floorIds(input.getFloors()).
					build();
			return this.mapDomainFactory.newContinentBuilder().
						id(input.getId()).
						name(input.getName()).
						dimension(input.getDimension()).
						map(map).
						build();
			//@formatter:on
		};
	}

	// METHODS

	@Override
	public Iterable<Continent> getContinents() {
		return Iterables.transform(this.mapContinentService.retrieveAllContinents(), this.continentConverter);
	}
}
