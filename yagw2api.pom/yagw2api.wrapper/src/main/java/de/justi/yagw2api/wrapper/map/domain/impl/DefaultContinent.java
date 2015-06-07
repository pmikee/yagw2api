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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.MapFloorService;
import de.justi.yagw2api.arenanet.MapService;
import de.justi.yagw2api.arenanet.dto.map.MapDTO;
import de.justi.yagw2api.arenanet.dto.map.MapFloorDTO;
import de.justi.yagw2api.common.tuple.UniformNumberTuple2;
import de.justi.yagw2api.wrapper.map.domain.Continent;
import de.justi.yagw2api.wrapper.map.domain.ContinentFloor;
import de.justi.yagw2api.wrapper.map.domain.Map;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;

final class DefaultContinent implements Continent {

	// STATIC
	public static ContinentBuilder builder(final MapDomainFactory mapDomainFactory, final MapService mapService, final MapFloorService mapFloorService) {
		return new DefaultContinentBuilder(checkNotNull(mapDomainFactory, "missing mapDomainFactory"), checkNotNull(mapService, "missing mapService"), checkNotNull(
				mapFloorService, "missing mapFloorService"));
	}

	// EMBEDDED
	public static final class DefaultContinentBuilder implements Continent.ContinentBuilder {
		// FIELDS

		@Nullable
		private String id = null;
		@Nullable
		private String name = null;
		@Nullable
		private UniformNumberTuple2<Integer> dimension = null;
		@Nullable
		private Integer minZoom = null;
		@Nullable
		private Integer maxZoom = null;

		private final Set<String> mapIds = Sets.newHashSet();
		private final Set<String> floorIndices = Sets.newHashSet();
		private final MapService mapService;
		private final MapFloorService mapFloorService;
		private final MapDomainFactory mapDomainFactory;

		// CONSTRUCTOR
		@Inject
		public DefaultContinentBuilder(final MapDomainFactory mapDomainFactory, final MapService mapService, final MapFloorService mapFloorService) {
			this.mapService = checkNotNull(mapService, "missing MapService");
			this.mapFloorService = checkNotNull(mapFloorService, "missing mapFloorService");
			this.mapDomainFactory = checkNotNull(mapDomainFactory, "missing mapDomainFactory");
		}

		// METHODS
		@Override
		public DefaultContinent build() {
			return new DefaultContinent(this);
		}

		@Override
		public DefaultContinentBuilder name(@Nullable final String name) {
			this.name = name;
			return this;
		}

		@Override
		public DefaultContinentBuilder id(@Nullable final String id) {
			this.id = id;
			return this;
		}

		@Override
		public DefaultContinentBuilder dimension(@Nullable final UniformNumberTuple2<Integer> dimension) {
			this.dimension = dimension;
			return this;
		}

		@Override
		public ContinentBuilder minZoom(final int zoom) {
			this.minZoom = zoom;
			return this;
		}

		@Override
		public ContinentBuilder maxZoom(final int zoom) {
			this.maxZoom = zoom;
			return this;
		}

		@Override
		public ContinentBuilder floorIndices(final Set<String> floorIndices) {
			this.floorIndices.clear();
			if (floorIndices != null) {
				this.floorIndices.addAll(floorIndices);
			}
			return this;
		}

		@Override
		public ContinentBuilder mapIds(final Set<String> mapIds) {
			this.mapIds.clear();
			if (mapIds != null) {
				this.mapIds.addAll(mapIds);
			}
			return this;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("dimension", this.dimension).add("floorIndices", this.floorIndices)
					.add("mapIds", this.mapIds).add("minZoom", this.minZoom).add("maxZoom", this.maxZoom).toString();
		}
	}

	private static final class MapDTOLoaderAndMapIdSupplier implements Function<String, Optional<? extends MapDTO>>, Supplier<NavigableSet<String>> {
		// FIELDS
		private final String continentId;
		private final String floorIndex;
		private final MapFloorService mapFloorService;

		@Nullable
		private volatile java.util.SortedMap<String, MapDTO> mapDTOsById = null;
		@Nullable
		private volatile NavigableSet<String> mapIds = null;

		// CONSTRUCTOR
		public MapDTOLoaderAndMapIdSupplier(final String continentId, final String floorIndex, final MapFloorService mapFloorService) {
			this.continentId = checkNotNull(continentId, "missing continentId");
			this.floorIndex = checkNotNull(floorIndex, "missing floorIndex");
			this.mapFloorService = checkNotNull(mapFloorService, "missing mapFloorService");
		}

		// METHODS
		private void initializeIfRequired() {
			if (this.mapDTOsById == null) {
				synchronized (this) {
					if (this.mapDTOsById == null) {
						final Optional<MapFloorDTO> floorDTO = this.mapFloorService.retrieveMapFloor(this.continentId, this.floorIndex);
						checkState(floorDTO.isPresent(), "missing floorDTO for continent=%s and floorIndex=%s", this.continentId, this.floorIndex);
						final Comparator<String> comparator = (mapIdA, mapIdB) -> Integer.valueOf(mapIdA).compareTo(Integer.valueOf(mapIdB));
						final ImmutableSortedMap.Builder<String, MapDTO> mapDTOsByIdBuilder = ImmutableSortedMap.orderedBy(comparator);
						for (Entry<String, ? extends MapDTO> entry : FluentIterable.from(floorDTO.get().getRegions().values()).transformAndConcat(
								(region) -> region.getMaps().entrySet())) {
							mapDTOsByIdBuilder.put(entry);
						}
						this.mapDTOsById = mapDTOsByIdBuilder.build();
						this.mapIds = ImmutableSortedSet.orderedBy(comparator).addAll(this.mapDTOsById.keySet()).build();
					}
				}
			}
		}

		@Override
		public NavigableSet<String> get() {
			this.initializeIfRequired();
			return this.mapIds;
		}

		@Override
		public Optional<MapDTO> apply(final String input) {
			checkNotNull(input, "missing input");
			this.initializeIfRequired();
			if (this.mapDTOsById.containsKey(input)) {
				return Optional.of(this.mapDTOsById.get(input));
			} else {
				return Optional.absent();
			}
		}

	}

	// FIELDS
	private final String id;
	private final String name;
	private final UniformNumberTuple2<Integer> dimension;
	private final int minZoom;
	private final int maxZoom;
	private final Iterable<ContinentFloor> floors;
	private final Iterable<Map> maps;
	private final SortedSet<String> floorIndices;
	private final SortedSet<String> mapIds;

	private final LoadingCache<String, Optional<ContinentFloor>> floorCache = CacheBuilder.newBuilder().build(new CacheLoader<String, Optional<ContinentFloor>>() {
		@Override
		public Optional<ContinentFloor> load(final String floorIndex) throws Exception {
			checkNotNull(floorIndex, "missing floorIndex");
			checkArgument(DefaultContinent.this.floorIndices.contains(floorIndex), "unknown floorIndex: %s", floorIndex);
			if (DefaultContinent.this.floorIndices.contains(floorIndex)) {
				final MapDTOLoaderAndMapIdSupplier loader = new MapDTOLoaderAndMapIdSupplier(DefaultContinent.this.id, floorIndex, DefaultContinent.this.mapFloorService);

				return Optional.of(DefaultContinent.this.mapDomainFactory.newContinentFloorBuilder().continentId(DefaultContinent.this.id).floorIndex(floorIndex)
						.minZoom(DefaultContinent.this.minZoom).maxZoom(DefaultContinent.this.maxZoom).mapIds(loader).mapDTOLoader(loader).build());
			} else {
				return Optional.absent();
			}
		}

	});
	private final MapCache mapCache;
	private final MapService mapService;
	private final MapFloorService mapFloorService;
	private final MapDomainFactory mapDomainFactory;

	// CONSTRUCTOR
	private DefaultContinent(final DefaultContinentBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.mapService = checkNotNull(builder.mapService, "missing mapService in %s", builder);
		this.mapFloorService = checkNotNull(builder.mapFloorService, "missing mapFloorService in %s", builder);
		this.mapDomainFactory = checkNotNull(builder.mapDomainFactory, "missing mapDomainFactory in %s", builder);

		this.id = checkNotNull(builder.id, "missing id in %s", builder);
		this.name = checkNotNull(builder.name, "missing name in %s", builder);
		this.dimension = checkNotNull(builder.dimension, "missing dimension in %s", builder);
		this.minZoom = checkNotNull(builder.minZoom, "missing minZoom in %s", builder);
		this.maxZoom = checkNotNull(builder.maxZoom, "missing maxZoom in %s", builder);
		this.floorIndices = ImmutableSortedSet.copyOf(checkNotNull(builder.floorIndices, "missing floorIds in %s", builder));
		this.mapIds = ImmutableSortedSet.copyOf(checkNotNull(builder.mapIds, "missing mapIds in %s", builder));

		this.mapCache = new MapCache((mapId) -> this.mapService.retrieveAllMaps().get().getMap(mapId), this.mapDomainFactory);

		this.floors = FluentIterable.from(this.floorIndices).transform((floorIndex) -> {
			checkNotNull(floorIndex, "missing floorIndex");
			return DefaultContinent.this.getFloor(floorIndex).get();
		});
		this.maps = FluentIterable.from(this.mapIds).transform((mapIndex) -> {
			checkNotNull(mapIndex, "missing mapIndex");
			return DefaultContinent.this.getMap(mapIndex).get();
		});
	}

	// METHODS

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public UniformNumberTuple2<Integer> getDimension() {
		return this.dimension;
	}

	@Override
	public Optional<ContinentFloor> getFloor(final String floorIndex) {
		try {
			return this.floorCache.get(floorIndex);
		} catch (ExecutionException e) {
			throw new Error(e);
		}
	}

	@Override
	public Iterable<ContinentFloor> getFloors() {
		return this.floors;
	}

	@Override
	public SortedSet<String> getFloorIdices() {
		return this.floorIndices;
	}

	@Override
	public int getMinZoom() {
		return this.minZoom;
	}

	@Override
	public int getMaxZoom() {
		return this.maxZoom;
	}

	@Override
	public Optional<Map> getMap(final String mapId) {
		checkArgument(this.mapIds.contains(mapId), "invalid mapId=%s for %s", mapId, this);
		return this.mapCache.get(mapId);
	}

	@Override
	public Iterable<Map> getMaps() {
		return this.maps;
	}

	@Override
	public int getTileTextureSize(final int zoom) {
		checkArgument(zoom >= this.minZoom && zoom <= this.maxZoom, "invalid zoom=%s for %s", zoom, this);
		return MapUtils.getTileTextureSize(zoom, this.minZoom, this.maxZoom);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("dimension", this.dimension).add("minZoom", this.minZoom)
				.add("maxZoom", this.maxZoom).add("floorIds", this.floorIndices).add("mapIds", this.mapIds).toString();
	}
}
