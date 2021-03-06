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
import static de.justi.yagw2api.wrapper.map.domain.impl.MapConstants.TILE_SIZE;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.v1.MapFloorService;
import de.justi.yagw2api.arenanet.v1.MapService;
import de.justi.yagw2api.arenanet.v1.dto.map.MapDTO;
import de.justi.yagw2api.arenanet.v1.dto.map.MapFloorDTO;
import de.justi.yagw2api.arenanet.v1.dto.map.MapPOIDTO;
import de.justi.yagw2api.common.tuple.IntTuple2;
import de.justi.yagw2api.common.tuple.IntTuple3;
import de.justi.yagw2api.common.tuple.IntTuple4;
import de.justi.yagw2api.common.tuple.NumberTuple4;
import de.justi.yagw2api.common.tuple.Tuples;
import de.justi.yagw2api.common.tuple.UniformNumberTuple4;
import de.justi.yagw2api.wrapper.map.domain.ContinentFloor;
import de.justi.yagw2api.wrapper.map.domain.Map;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;
import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagw2api.wrapper.map.domain.NoSuchMapTileException;
import de.justi.yagw2api.wrapper.map.domain.POI;
import de.justi.yagw2api.wrapper.map.domain.POI.POIBuilder;

final class DefaultContinentFloor implements ContinentFloor {
	// CONSTS
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultContinentFloor.class);

	// STATIC
	public static final ContinentFloorBuilder builder(final MapFloorService mapFloorService, final MapService mapService, final MapDomainFactory mapDomainFactory) {
		return new DefaultContinentFloorBuilder(checkNotNull(mapFloorService, "missing mapFloorService"), checkNotNull(mapService, "missing mapService"), checkNotNull(
				mapDomainFactory, "missing mapDomainFactory"));
	}

	// EMBEDDED
	private static final class DefaultContinentFloorBuilder implements ContinentFloorBuilder {
		// FIELDS
		private final MapFloorService mapFloorService;
		private final MapDomainFactory mapDomainFactory;

		@Nullable
		private Supplier<NavigableSet<String>> mapIdSupplier = null;
		@Nullable
		private Function<String, Optional<MapDTO>> mapDTOLoader = null;
		@Nullable
		private String floorIndex = null;
		@Nullable
		private String continentId = null;
		@Nullable
		private Integer minZoom = null;
		@Nullable
		private Integer maxZoom = null;

		// CONSTRUCTOR
		@Inject
		private DefaultContinentFloorBuilder(final MapFloorService mapFloorService, final MapService mapService, final MapDomainFactory mapDomainFactory) {
			this.mapFloorService = checkNotNull(mapFloorService, "missing mapFloorService");
			this.mapDomainFactory = checkNotNull(mapDomainFactory, "missing mapDomainFactory");
		}

		// METHODS
		@Override
		public ContinentFloorBuilder continentId(final String continentId) {
			this.continentId = continentId;
			return this;
		}

		@Override
		public ContinentFloorBuilder floorIndex(final String floorIndex) {
			this.floorIndex = floorIndex;
			return this;
		}

		@Override
		public ContinentFloorBuilder minZoom(final int minZoom) {
			this.minZoom = minZoom;
			return this;
		}

		@Override
		public ContinentFloorBuilder maxZoom(final int maxZoom) {
			this.maxZoom = maxZoom;
			return this;
		}

		@Override
		public ContinentFloorBuilder mapIds(final Supplier<NavigableSet<String>> mapIdSupplier) {
			this.mapIdSupplier = mapIdSupplier;
			return this;
		}

		@Override
		public ContinentFloorBuilder mapDTOLoader(final Function<String, Optional<MapDTO>> mapDTOLoader) {
			this.mapDTOLoader = mapDTOLoader;
			return this;
		}

		@Override
		public ContinentFloor build() {
			return new DefaultContinentFloor(this);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("continentId", this.continentId).add("floorIndex", this.floorIndex).add("minZoom", this.minZoom)
					.add("maxZoom", this.maxZoom).toString();
		}
	}

	// FIELDS
	private final MapFloorService mapFloorService;
	private final Function<String, Optional<MapDTO>> mapDTOLoader;
	private final MapDomainFactory mapDomainFactory;
	private final Supplier<NavigableSet<String>> mapIdSupplier;
	private final MapCache<MapDTO> mapCache;
	private final Supplier<Iterable<Map>> maps = Suppliers.memoize(new Supplier<Iterable<Map>>() {
		@Override
		public Iterable<Map> get() {
			return FluentIterable.from(DefaultContinentFloor.this.mapIdSupplier.get()).transform((mapIndex) -> {
				checkNotNull(mapIndex, "missing mapIndex");
				return DefaultContinentFloor.this.getMap(mapIndex).get();
			});
		}
	});
	private final Supplier<Iterable<Map>> mostSignificantMaps = Suppliers.memoize(new Supplier<Iterable<Map>>() {
		@Override
		public Iterable<Map> get() {
			return new Iterable<Map>() {
				@Override
				public Iterator<Map> iterator() {
					return new MostSignificantMapsIterator(DefaultContinentFloor.this.maps.get().iterator());
				}
			};
		}

	});
	private final String floorIndex;
	private final int minZoom;
	private final int maxZoom;
	private final Supplier<IntTuple2> textureSize = Suppliers.memoize(new Supplier<IntTuple2>() {
		@Override
		public IntTuple2 get() {
			final com.google.common.base.Optional<MapFloorDTO> optionalFloorDTO = DefaultContinentFloor.this.mapFloorService.retrieveMapFloor(
					DefaultContinentFloor.this.continentId, DefaultContinentFloor.this.floorIndex);
			if (optionalFloorDTO.isPresent()) {
				return optionalFloorDTO.get().getTextureDimension();
			} else {
				return Tuples.of(0, 0);
			}
		}
	});
	private final Supplier<IntTuple4> clampedTextureSize = Suppliers.memoize(new Supplier<IntTuple4>() {
		@Override
		public IntTuple4 get() {
			final com.google.common.base.Optional<MapFloorDTO> optionalFloorDTO = DefaultContinentFloor.this.mapFloorService.retrieveMapFloor(
					DefaultContinentFloor.this.continentId, DefaultContinentFloor.this.floorIndex);
			if (optionalFloorDTO.isPresent()) {
				if (optionalFloorDTO.get().getClampedView().isPresent()) {
					final NumberTuple4<Integer, Integer, Integer, Integer> clamped = optionalFloorDTO.get().getClampedView().get();
					final int x1 = clamped.v1() / TILE_SIZE;
					final int y1 = clamped.v2() / TILE_SIZE;
					final int x2 = (clamped.v3() / TILE_SIZE) + ((clamped.v3() % TILE_SIZE == 0) ? 0 : 1);
					final int y2 = (clamped.v4() / TILE_SIZE) + ((clamped.v4() % TILE_SIZE == 0) ? 0 : 1);
					return Tuples.of(x1 * TILE_SIZE, y1 * TILE_SIZE, x2 * TILE_SIZE, y2 * TILE_SIZE);
				} else {
					return Tuples.of(0, 0, optionalFloorDTO.get().getTextureDimension().v1().intValue(), optionalFloorDTO.get().getTextureDimension().v2().intValue());
				}
			} else {
				return Tuples.of(0, 0, 0, 0);
			}
		}
	});
	private final String continentId;
	/**
	 * <p>
	 * Tuple2<Integer, Tuple2<Integer, Integer>>
	 * </p>
	 * <p>
	 * Tuple2<{zoom},{position}>
	 * </p>
	 */
	private final LoadingCache<IntTuple3, MapTile> tileCache = CacheBuilder.newBuilder().build(new CacheLoader<IntTuple3, MapTile>() {
		@Override
		public MapTile load(final IntTuple3 key) throws Exception {
			checkNotNull(key, "missing key");
			checkNotNull(key.v1(), "missing zoom in %s", key);
			checkNotNull(key.v2(), "missing position x in %s", key);
			checkNotNull(key.v3(), "missing position y in %s", key);
			if (DefaultContinentFloor.this.isTileAvailable(key.v2(), key.v3(), key.v1())) {
				return DefaultContinentFloor.this.mapDomainFactory.newMapTileBuilder().continentId(DefaultContinentFloor.this.continentId)
						.floorIndex(DefaultContinentFloor.this.floorIndex).zoom(key.v1()).position(Tuples.of(key.v2Int(), key.v3Int())).build();
			} else {
				return DefaultContinentFloor.this.mapDomainFactory.newMapUnavailableTileBuilder().continentId(DefaultContinentFloor.this.continentId)
						.floorIndex(DefaultContinentFloor.this.floorIndex).zoom(key.v1()).position(Tuples.of(key.v2Int(), key.v3Int())).build();
			}
		}
	});

	// CONSTRUCTOR
	private DefaultContinentFloor(final DefaultContinentFloorBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.mapFloorService = checkNotNull(builder.mapFloorService, "missing mapFloorService in %s", builder);
		this.mapDomainFactory = checkNotNull(builder.mapDomainFactory, "missing mapDomainFactory in %s", builder);
		this.mapDTOLoader = checkNotNull(builder.mapDTOLoader, "missing mapDTOLoader in %s", builder);
		this.floorIndex = checkNotNull(builder.floorIndex, "missing floorIndex in %s", builder);
		this.continentId = checkNotNull(builder.continentId, "missing continentId in %s", builder);
		this.minZoom = checkNotNull(builder.minZoom, "missing minZoom in %s", builder);
		this.maxZoom = checkNotNull(builder.maxZoom, "missing maxZoom in %s", builder);
		this.mapIdSupplier = Suppliers.memoize(checkNotNull(builder.mapIdSupplier, "missing mapIdSupplier in %s", builder));

		final Function<String, Iterable<POI>> poiLoader = new Function<String, Iterable<POI>>() {

			@Override
			public Iterable<POI> apply(final String mapId) {
				checkNotNull(mapId, "missing mapId");
				final MapFloorDTO floorDTO = DefaultContinentFloor.this.mapFloorService.retrieveMapFloor(DefaultContinentFloor.this.continentId,
						DefaultContinentFloor.this.floorIndex).get();
				final Iterable<MapPOIDTO> poiDTOs = FluentIterable.from(floorDTO.getRegions().values()).transformAndConcat((region) -> region.getMaps().values())
						.transformAndConcat((mapRegionMap) -> mapRegionMap.getPOIs());
				final java.util.Map<Integer, POI> pois = Maps.newHashMap();
				for (MapPOIDTO poiDTO : poiDTOs) {
					if (!pois.containsKey(poiDTO.getId())) {
						final POIBuilder<?, ?> poiBuilder;
						switch (poiDTO.getType()) {
							case LANDMARK:
								poiBuilder = DefaultContinentFloor.this.mapDomainFactory.newPOILandmarkBuilder();
								break;
							case VISTA:
								poiBuilder = DefaultContinentFloor.this.mapDomainFactory.newPOIVistaBuilder();
								break;
							case WAYPOINT:
								poiBuilder = DefaultContinentFloor.this.mapDomainFactory.newPOIWaypointBuilder();
								break;
							case UNLOCK:
								poiBuilder = DefaultContinentFloor.this.mapDomainFactory.newPOIUnlockBuilder();
								break;
							default:
								throw new UnsupportedOperationException("poiType is not supported: " + poiDTO);
						}
						poiBuilder.id(String.valueOf(poiDTO.getId())).name(poiDTO.getName()).location(poiDTO.getCoordinates());
						pois.put(poiDTO.getId(), poiBuilder.build());
					}
				}
				return ImmutableSet.copyOf(pois.values());
			}
		};

		this.mapCache = new MapCache<MapDTO>(this.mapDTOLoader, poiLoader, this.mapDomainFactory);
	}

	// METHODS

	private int tile2Texture(final int tile, final int zoom) {
		return MapUtils.calculateTileTextureSize(zoom, this.minZoom, this.maxZoom) * tile;
	}

	private int texture2Tile(final int texture, final int zoom) {
		return texture / MapUtils.calculateTileTextureSize(zoom, this.minZoom, this.maxZoom);
	}

	private IntTuple4 texture2Tile(final UniformNumberTuple4<Integer> texture, final int zoom) {
		checkNotNull(texture, "missing texture");
		return Tuples
				.of(this.texture2Tile(texture.v1(), zoom), this.texture2Tile(texture.v2(), zoom), this.texture2Tile(texture.v3(), zoom), this.texture2Tile(texture.v4(), zoom));
	}

	private final boolean isTileAvailable(final int x, final int y, final int zoom) {
		int x1Texture = this.tile2Texture(x, zoom);
		int x2Texture = this.tile2Texture(x + 1, zoom);
		int y1Texture = this.tile2Texture(y, zoom);
		int y2Texture = this.tile2Texture(y + 1, zoom);
		final IntTuple4 tileRegion = Tuples.of(x1Texture, y1Texture, x2Texture, y2Texture);
		if (tileRegion.overlaps(this.getClampedTextureDimension())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public MapTile getTile(final int x, final int y, final int zoom) throws NoSuchMapTileException {
		try {
			return this.tileCache.get(Tuples.of(zoom, x, y));
		} catch (ExecutionException e) {
			throw new NoSuchMapTileException();
		}
	}

	@Override
	public IntTuple2 getTextureDimension() {
		return this.textureSize.get();
	}

	@Override
	public IntTuple4 getClampedTextureDimension() {
		return this.clampedTextureSize.get();
	}

	@Override
	public IntTuple4 getClampedTileIndexDimension(final int zoom) {
		return this.texture2Tile(this.getClampedTextureDimension(), zoom);
	}

	@Override
	public NavigableSet<String> getMapIds() {
		return this.mapIdSupplier.get();
	}

	@Override
	public Optional<Map> getMap(final String mapId) {
		return this.mapCache.get(mapId);
	}

	@Override
	public Iterable<Map> getMaps() {
		return this.maps.get();
	}

	@Override
	public Iterable<Map> getMostSignificantMaps() {
		return this.mostSignificantMaps.get();
	}

	@Override
	public String getIndex() {
		return this.floorIndex;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("continentId", this.continentId).add("floorIndex", this.floorIndex).add("minZoom", this.minZoom).add("maxZoom", this.maxZoom)
				.add("texture", this.getTextureDimension()).add("clamped", this.getClampedTextureDimension()).toString();
	}
}
