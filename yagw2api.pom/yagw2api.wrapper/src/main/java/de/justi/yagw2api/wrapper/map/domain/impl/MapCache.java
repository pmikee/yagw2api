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

import java.util.concurrent.ExecutionException;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.justi.yagw2api.arenanet.dto.map.MapDTO;
import de.justi.yagw2api.wrapper.map.domain.Map;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;

final class MapCache {

	// EMBEDDED

	// FIELDS
	private final MapDomainFactory mapDomainFactory;
	private final Function<String, MapDTO> dtoLoadingFunction;
	private final LoadingCache<String, Map> mapCache = CacheBuilder.newBuilder().recordStats().build(new CacheLoader<String, Map>() {

		@Override
		public Map load(final String mapId) throws Exception {
			checkNotNull(mapId, "missing mapId");
			final MapDTO mapDTO = MapCache.this.dtoLoadingFunction.apply(mapId);
			return MapCache.this.mapDomainFactory.newMapBuilder().id(mapId).name(mapDTO.getName()).defaultFloorIndex(mapDTO.getDefaultFloor())
					.boundsOnContinent(mapDTO.getBoundsOnContinent()).build();
		}
	});

	// CONSTRUCTOR
	public MapCache(final Function<String, MapDTO> dtoLoadingFunction, final MapDomainFactory mapDomainFactory) {
		this.mapDomainFactory = checkNotNull(mapDomainFactory, "missing mapDomainFactory");
		this.dtoLoadingFunction = checkNotNull(dtoLoadingFunction, "missing dtoLoadingFunction");
	}

	// METHODS
	public Map get(final String id) {
		try {
			return this.mapCache.get(id);
		} catch (ExecutionException e) {
			throw new Error(e);
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("stats", this.mapCache.stats()).toString();
	}

}
