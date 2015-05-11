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

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;

import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;
import de.justi.yagw2api.wrapper.map.domain.MapFloorTiles;
import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagw2api.wrapper.map.domain.NoSuchMapTileException;
import de.justi.yagwapi.common.Tuple2;
import de.justi.yagwapi.common.Tuples;

final class DefaultMapFloorTiles implements MapFloorTiles {
	// CONSTS

	// STATIC
	public static final MapFloorTilesBuilder builder(final MapDomainFactory mapDomainFactory) {
		return new DefaultMapFloorTilesBuilder(checkNotNull(mapDomainFactory, "missing mapDomainFactory"));
	}

	// EMBEDDED
	private static final class DefaultMapFloorTilesBuilder implements MapFloorTilesBuilder {
		// FIELDS
		private final MapDomainFactory mapDomainFactory;
		@Nullable
		private Integer floorIndex = null;
		@Nullable
		private String continentId = null;

		// CONSTRUCTOR
		@Inject
		private DefaultMapFloorTilesBuilder(final MapDomainFactory mapDomainFactory) {
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
		public MapFloorTiles build() {
			return new DefaultMapFloorTiles(this);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).toString();
		}

	}

	// FIELDS
	private final MapDomainFactory mapDomainFactory;
	private final int floorIndex;
	private final String continentId;
	/**
	 * <p>
	 * Tuple2<Integer, Tuple2<Integer, Integer>>
	 * </p>
	 * <p>
	 * Tuple2<{zoom},{position}>
	 * </p>
	 */
	private final LoadingCache<Tuple2<Integer, Tuple2<Integer, Integer>>, MapTile> tileCache = CacheBuilder.newBuilder().build(
			new CacheLoader<Tuple2<Integer, Tuple2<Integer, Integer>>, MapTile>() {
				@Override
				public MapTile load(final Tuple2<Integer, Tuple2<Integer, Integer>> key) throws Exception {
					checkNotNull(key, "missing key");
					checkNotNull(key.v1(), "missing zoom in %s", key);
					checkNotNull(key.v2(), "missing position in %s", key);
					return DefaultMapFloorTiles.this.mapDomainFactory.newMapTileBuilder().continentId(DefaultMapFloorTiles.this.continentId)
							.floorIndex(DefaultMapFloorTiles.this.floorIndex).zoom(key.v1()).position(key.v2()).build();
				}
			});

	// CONSTRUCTOR
	private DefaultMapFloorTiles(final DefaultMapFloorTilesBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.mapDomainFactory = checkNotNull(builder.mapDomainFactory, "missing mapDomainFactory in %s", builder);
		this.floorIndex = checkNotNull(builder.floorIndex, "missing floorIndex in %s", builder);
		this.continentId = checkNotNull(builder.continentId, "missing continentId in %s", builder);
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

	@Override
	public MapTile getTile(final int x, final int y, final int zoom) throws NoSuchMapTileException {
		try {
			return this.tileCache.get(Tuples.of(zoom, Tuples.of(x, y)));
		} catch (ExecutionException e) {
			throw new NoSuchMapTileException();
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("continentId", this.continentId).add("floorIndex", this.floorIndex).toString();
	}

}
