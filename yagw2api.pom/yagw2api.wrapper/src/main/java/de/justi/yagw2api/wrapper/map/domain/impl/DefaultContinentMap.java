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

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.MapFloorService;
import de.justi.yagw2api.arenanet.dto.map.MapFloorDTO;
import de.justi.yagw2api.wrapper.map.domain.ContinentMap;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;
import de.justi.yagw2api.wrapper.map.domain.MapFloor;
import de.justi.yagw2api.wrapper.map.domain.MapFloorTiles;

final class DefaultContinentMap implements ContinentMap {

	// STATIC
	public static ContinentMapBuilder builder(final MapDomainFactory mapDomainFactory, final MapFloorService mapFloorService) {
		return new DefaultContinentMapBuilder(checkNotNull(mapDomainFactory, "missing mapDomainFactory"), checkNotNull(mapFloorService, "missing mapFloorService"));
	}

	// EMBEDDED
	static final class DefaultContinentMapBuilder implements ContinentMap.ContinentMapBuilder {
		// FIELDS
		@Nullable
		private String continentId = null;
		private final Set<Integer> floorIds = Sets.newHashSet();
		private final MapFloorService mapFloorService;
		private final MapDomainFactory mapDomainFactory;

		// CONSTRUCTOR
		@Inject
		public DefaultContinentMapBuilder(final MapDomainFactory mapDomainFactory, final MapFloorService mapFloorService) {
			this.mapDomainFactory = checkNotNull(mapDomainFactory, "missing mapDomainFactory");
			this.mapFloorService = checkNotNull(mapFloorService, "missing mapFloorService");
		}

		// METHODS
		@Override
		public ContinentMap build() {
			return new DefaultContinentMap(this);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("continentId", this.continentId).add("floorIds", this.floorIds).toString();
		}

		@Override
		public ContinentMapBuilder continentId(final String continentId) {
			this.continentId = continentId;
			return this;
		}

		@Override
		public ContinentMapBuilder floorIds(final Set<Integer> floorIds) {
			this.floorIds.clear();
			if (floorIds != null) {
				this.floorIds.addAll(floorIds);
			}
			return this;
		}

	}

	// FIELDS
	private final LoadingCache<Integer, MapFloorTiles> floorTilesCache = CacheBuilder.newBuilder().build(new CacheLoader<Integer, MapFloorTiles>() {

		@Override
		public MapFloorTiles load(final Integer floorIndex) throws Exception {
			checkNotNull(floorIndex, "missing floorIndex");
			final Optional<MapFloorDTO> floorDTO = DefaultContinentMap.this.mapFloorService.retrieveMapFloor(DefaultContinentMap.this.continentId, floorIndex);
			if (floorDTO.isPresent()) {
				return DefaultContinentMap.this.mapDomainFactory.newMapFloorTilesBuilder().build();
			} else {
				throw new Error("Missing map floor for floorIndex=" + floorIndex + " and continentId=" + DefaultContinentMap.this.continentId);
			}
		}

	});
	private final String continentId;
	private final MapFloorService mapFloorService;
	private final MapDomainFactory mapDomainFactory;
	private final List<MapFloor> mapFloors;

	// CONSTRUCTOR
	private DefaultContinentMap(final DefaultContinentMapBuilder builder) {
		this.continentId = checkNotNull(builder.continentId, "missing continentId in %s", builder);
		this.mapFloorService = checkNotNull(builder.mapFloorService, "missing mapFloorService in %s", builder);
		this.mapDomainFactory = checkNotNull(builder.mapDomainFactory, "missing mapDomainFactory in %s", builder);
		this.mapFloors = FluentIterable.from(checkNotNull(builder.floorIds, "missing floorIds"))
				.<MapFloor> transform(floorId -> this.mapDomainFactory.newMapFloorBuilder().index(floorId).build()).toList();
	}

	// METHODS

	@Override
	public MapFloorTiles getFloorTiles(final MapFloor floor) {
		checkNotNull(floor, "missing floor");
		try {
			return this.floorTilesCache.get(floor.getIndex());
		} catch (ExecutionException e) {
			throw new Error(e);
		}
	}

	@Override
	public List<MapFloor> getFloors() {
		return this.mapFloors;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("mapFloors", this.mapFloors).toString();
	}
}
