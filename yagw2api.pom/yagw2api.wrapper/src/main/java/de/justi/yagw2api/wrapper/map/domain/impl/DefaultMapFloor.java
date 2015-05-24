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

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.MapFloorService;
import de.justi.yagw2api.arenanet.dto.map.MapFloorDTO;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;
import de.justi.yagw2api.wrapper.map.domain.MapFloor;
import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagw2api.wrapper.map.domain.NoSuchMapTileException;
import de.justi.yagwapi.common.Tuple2;
import de.justi.yagwapi.common.Tuple3;
import de.justi.yagwapi.common.Tuple4;
import de.justi.yagwapi.common.Tuples;

final class DefaultMapFloor implements MapFloor {
	// CONSTS
	private static final int TILE_SIZE = 256;
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMapFloor.class);

	// STATIC
	public static final MapFloorTilesBuilder builder(final MapFloorService mapFloorService, final MapDomainFactory mapDomainFactory) {
		return new DefaultMapFloorTilesBuilder(checkNotNull(mapFloorService, "missing mapFloorService"), checkNotNull(mapDomainFactory, "missing mapDomainFactory"));
	}

	// EMBEDDED
	private static final class DefaultMapFloorTilesBuilder implements MapFloorTilesBuilder {
		// FIELDS
		private final MapFloorService mapFloorService;
		private final MapDomainFactory mapDomainFactory;
		@Nullable
		private Integer floorIndex = null;
		@Nullable
		private String continentId = null;
		@Nullable
		private Integer minZoom = null;
		@Nullable
		private Integer maxZoom = null;

		// CONSTRUCTOR
		@Inject
		private DefaultMapFloorTilesBuilder(final MapFloorService mapFloorService, final MapDomainFactory mapDomainFactory) {
			this.mapFloorService = checkNotNull(mapFloorService, "missing mapFloorService");
			this.mapDomainFactory = checkNotNull(mapDomainFactory, "missing mapDomainFactory");
		}

		// METHODS
		@Override
		public MapFloorTilesBuilder continentId(final String continentId) {
			this.continentId = continentId;
			return this;
		}

		@Override
		public MapFloorTilesBuilder floorIndex(final int floorIndex) {
			this.floorIndex = floorIndex;
			return this;
		}

		@Override
		public MapFloorTilesBuilder minZoom(final int minZoom) {
			this.minZoom = minZoom;
			return this;
		}

		@Override
		public MapFloorTilesBuilder maxZoom(final int maxZoom) {
			this.maxZoom = maxZoom;
			return this;
		}

		@Override
		public MapFloor build() {
			return new DefaultMapFloor(this);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("continentId", this.continentId).add("floorIndex", this.floorIndex).add("minZoom", this.minZoom)
					.add("maxZoom", this.maxZoom).toString();
		}
	}

	// FIELDS
	private final MapFloorService mapFloorService;
	private final MapDomainFactory mapDomainFactory;
	private final int floorIndex;
	private final int minZoom;
	private final int maxZoom;
	private final Supplier<Tuple2<Integer, Integer>> floorTextureSize = Suppliers.memoize(new Supplier<Tuple2<Integer, Integer>>() {
		@Override
		public Tuple2<Integer, Integer> get() {
			final com.google.common.base.Optional<MapFloorDTO> optionalFloorDTO = DefaultMapFloor.this.mapFloorService.retrieveMapFloor(DefaultMapFloor.this.continentId,
					DefaultMapFloor.this.floorIndex);
			if (optionalFloorDTO.isPresent()) {
				return optionalFloorDTO.get().getTextureDimension();
			} else {
				return Tuples.of(0, 0);
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
	private final LoadingCache<Tuple3<Integer, Integer, Integer>, MapTile> tileCache = CacheBuilder.newBuilder().build(
			new CacheLoader<Tuple3<Integer, Integer, Integer>, MapTile>() {
				@Override
				public MapTile load(final Tuple3<Integer, Integer, Integer> key) throws Exception {
					checkNotNull(key, "missing key");
					checkNotNull(key.v1(), "missing zoom in %s", key);
					checkNotNull(key.v2(), "missing position x in %s", key);
					checkNotNull(key.v3(), "missing position y in %s", key);
					if (DefaultMapFloor.this.isTileAvailable(key.v2(), key.v3(), key.v1())) {
						return DefaultMapFloor.this.mapDomainFactory.newMapTileBuilder().continentId(DefaultMapFloor.this.continentId).floorIndex(DefaultMapFloor.this.floorIndex)
								.zoom(key.v1()).position(Tuples.of(key.v2(), key.v3())).build();
					} else {
						return DefaultMapFloor.this.mapDomainFactory.newMapUnavailableTileBuilder().continentId(DefaultMapFloor.this.continentId)
								.floorIndex(DefaultMapFloor.this.floorIndex).zoom(key.v1()).position(Tuples.of(key.v2(), key.v3())).build();
					}
				}
			});

	// CONSTRUCTOR
	private DefaultMapFloor(final DefaultMapFloorTilesBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.mapFloorService = checkNotNull(builder.mapFloorService, "missing mapFloorService in %s", builder);
		this.mapDomainFactory = checkNotNull(builder.mapDomainFactory, "missing mapDomainFactory in %s", builder);
		this.floorIndex = checkNotNull(builder.floorIndex, "missing floorIndex in %s", builder);
		this.continentId = checkNotNull(builder.continentId, "missing continentId in %s", builder);
		this.minZoom = checkNotNull(builder.minZoom, "missing minZoom in %s", builder);
		this.maxZoom = checkNotNull(builder.maxZoom, "missing maxZoom in %s", builder);
	}

	// METHODS
	@Override
	public Optional<MapTile> getTileUnchecked(final int x, final int y, final int zoom) {
		try {
			return Optional.of(this.getTile(x, y, zoom));
		} catch (NoSuchMapTileException e) {
			return Optional.empty();
		}
	}

	final int tile2Texture(final int tile, final int zoom) {
		return this.getTileTextureSize(zoom) * tile;
	}

	final Tuple2<Integer, Integer> tile2Texture(final Tuple2<Integer, Integer> tile, final int zoom) {
		checkNotNull(tile, "missing tile");
		return Tuples.of(this.tile2Texture(tile.v1(), zoom), this.tile2Texture(tile.v2(), zoom));
	}

	final int texture2Tile(final int texture, final int zoom) {
		return texture / this.getTileTextureSize(zoom);
	}

	final Tuple2<Integer, Integer> texture2Tile(final Tuple2<Integer, Integer> texture, final int zoom) {
		checkNotNull(texture, "missing texture");
		return Tuples.of(this.texture2Tile(texture.v1(), zoom), this.texture2Tile(texture.v2(), zoom));
	}

	final Tuple4<Integer, Integer, Integer, Integer> texture2Tile(final Tuple4<Integer, Integer, Integer, Integer> texture, final int zoom) {
		checkNotNull(texture, "missing texture");
		return Tuples
				.of(this.texture2Tile(texture.v1(), zoom), this.texture2Tile(texture.v2(), zoom), this.texture2Tile(texture.v3(), zoom), this.texture2Tile(texture.v4(), zoom));
	}

	/**
	 * Two rectangles do not overlap if one of the following conditions is true:
	 * <ol>
	 * <li>One rectangle is above top edge of other rectangle.</li>
	 * <li>One rectangle is on left side of left edge of other rectangle.</li>
	 * </ol>
	 *
	 * @param left
	 * @param right
	 * @return
	 */
	final boolean overlaps(final Tuple4<Integer, Integer, Integer, Integer> left, final Tuple4<Integer, Integer, Integer, Integer> right) {

		// If one rectangle is on left side of other
		if (left.v1() > right.v3() || right.v1() > left.v3()) {
			return false;
		}

		// If one rectangle is above other
		if (left.v2() > right.v4() || right.v2() > left.v4()) {
			return false;
		}

		return true;
	}

	private final boolean isTileAvailable(final int x, final int y, final int zoom) {
		final com.google.common.base.Optional<MapFloorDTO> optionalFloorDTO = this.mapFloorService.retrieveMapFloor(this.continentId, this.floorIndex);
		if (optionalFloorDTO.isPresent()) {
			final MapFloorDTO floorDTO = optionalFloorDTO.get();
			final Tuple2<Integer, Integer> topLeft = this.tile2Texture(Tuples.of(x, y), zoom);
			final Tuple4<Integer, Integer, Integer, Integer> tileRegion = Tuples.merge(topLeft, topLeft.v1() + this.getTileTextureSize(zoom),
					topLeft.v2() + this.getTileTextureSize(zoom));
			final Tuple4<Integer, Integer, Integer, Integer> availableRegion = floorDTO.getClampedView().or(new Supplier<Tuple4<Integer, Integer, Integer, Integer>>() {
				@Override
				public Tuple4<Integer, Integer, Integer, Integer> get() {
					return Tuples.of(0, 0, floorDTO.getTextureDimension().v1(), floorDTO.getTextureDimension().v2());
				}
			});
			if (this.overlaps(tileRegion, availableRegion)) {
				return true;
			} else {
				// outside of texture dimension
				LOGGER.trace("{}/{}|{} is NOT available (availableRegion: {}|) @ {}", x, y, topLeft, availableRegion, this.texture2Tile(availableRegion, zoom), this);
				return false;
			}
		} else {
			// floor does not exist
			LOGGER.trace("{}/{} is NOT available (floor: {}) @ {}", x, y, this.floorIndex, this);
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
	public Tuple2<Integer, Integer> getTextureDimension() {
		return this.floorTextureSize.get();
	}

	@Override
	public Tuple2<Integer, Integer> getTileIndexDimension(final int zoom) {
		return DefaultMapFloor.this.texture2Tile(DefaultMapFloor.this.getTextureDimension(), zoom);
	}

	@Override
	public int getTileTextureSize(final int zoom) {
		checkArgument(zoom >= this.minZoom && zoom <= this.maxZoom, "invalid zoom=%s for %s", zoom, this);
		return TILE_SIZE * (int) Math.pow(2, this.maxZoom - zoom);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("continentId", this.continentId).add("floorIndex", this.floorIndex).add("minZoom", this.minZoom).add("maxZoom", this.maxZoom)
				.toString();
	}
}
