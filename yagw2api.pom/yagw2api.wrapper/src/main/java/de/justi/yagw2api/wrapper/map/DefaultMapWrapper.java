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

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.v1.MapContinentService;
import de.justi.yagw2api.arenanet.v1.MapService;
import de.justi.yagw2api.arenanet.v1.dto.map.MapContinentWithIdDTO;
import de.justi.yagw2api.wrapper.map.domain.Continent;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;

public final class DefaultMapWrapper implements MapWrapper {

	// CONSTS

	// EMBEDDED

	// FIELDS
	private final EventBus eventBus;
	private final MapDomainFactory mapDomainFactory;
	private final MapContinentService mapContinentService;
	private final MapService mapService;
	private final List<Continent> continents;
	private final FluentIterable<Continent> fluentContinents;
	private final Function<MapContinentWithIdDTO, Continent> continentDTOConverter;

	// CONSTRUCTOR
	@Inject
	public DefaultMapWrapper(final EventBus eventBus, final MapDomainFactory mapDomainFactory, final MapContinentService mapContinentService, final MapService mapService) {
		this.eventBus = checkNotNull(eventBus, "missing eventBus");
		this.mapContinentService = checkNotNull(mapContinentService, "missing mapContinentService");
		this.mapDomainFactory = checkNotNull(mapDomainFactory, "missing mapDomainFactory");
		this.mapService = checkNotNull(mapService, "missing mapService");
		this.continentDTOConverter = new MapContinentWithIdDTO2ContinentFunction(this.mapService, this.mapDomainFactory);
		this.continents = FluentIterable.from(this.mapContinentService.retrieveAllContinents()).transform(this.continentDTOConverter).toList();
		this.fluentContinents = FluentIterable.from(this.continents);
	}

	// METHODS

	@Override
	public List<Continent> getContinents() {
		return this.continents;
	}

	@Override
	public EventBus getChannel() {
		return this.eventBus;
	}

	@Override
	public Optional<Continent> findContinentById(final String id) {
		checkNotNull(id, "missing id");
		return this.fluentContinents.firstMatch(MapPredicates.continentIdEquals(id));
	}

	@Override
	public Continent getContinentById(final String id) throws NoSuchContinentException {
		checkNotNull(id, "missing id");
		final Optional<Continent> continent = this.findContinentById(id);
		if (continent.isPresent()) {
			return continent.get();
		} else {
			throw new NoSuchContinentException();
		}
	}
}
