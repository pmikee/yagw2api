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

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.MapService;
import de.justi.yagw2api.arenanet.dto.map.MapContinentWithIdDTO;
import de.justi.yagw2api.wrapper.map.domain.Continent;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;

final class MapContinentWithIdDTO2ContinentFunction implements Function<MapContinentWithIdDTO, Continent> {
	// FIELDS
	private final MapService mapService;
	private final MapDomainFactory mapDomainFactory;

	// CONSTRUCTOR
	@Inject
	public MapContinentWithIdDTO2ContinentFunction(final MapService mapService, final MapDomainFactory mapDomainFactory) {
		this.mapService = checkNotNull(mapService, "missing mapService");
		this.mapDomainFactory = checkNotNull(mapDomainFactory, "missing mapDomainFactory");
	}

	// METHODS
	@Override
	public Continent apply(@Nullable final MapContinentWithIdDTO c) {
		checkNotNull(c, "missing input");
		final Set<String> mapIds = FluentIterable.from(this.mapService.retrieveAllMaps().get().getMaps().entrySet())
				.filter((mapEntry) -> mapEntry.getValue().getContinentId().equals(c.getId())).transform((mapEntry) -> mapEntry.getKey()).toSet();
		//@formatter:off
		return this.mapDomainFactory.newContinentBuilder().
					id(c.getId()).
					name(c.getName()).
					mapIds(mapIds).
					floorIndices(c.getFloors()).
					dimension(c.getDimension()).
					minZoom(c.getMinZoom()).
					maxZoom(c.getMaxZoom()).
				build();
		//@formatter:on
	}
}