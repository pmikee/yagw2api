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

import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import de.justi.yagw2api.wrapper.map.domain.Continent;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;
import de.justi.yagw2api.wrapper.map.domain.MapFloor;
import de.justi.yagwapi.common.Tuple2;

final class DefaultContinent implements Continent {

	// STATIC
	public static ContinentBuilder builder(final MapDomainFactory mapDomainFactory) {
		return new DefaultContinentBuilder(checkNotNull(mapDomainFactory, "missing mapDomainFactory"));
	}

	// EMBEDDED
	public static final class DefaultContinentBuilder implements Continent.ContinentBuilder {
		// FIELDS

		@Nullable
		private String id = null;
		@Nullable
		private String name = null;
		@Nullable
		private Tuple2<Integer, Integer> dimension = null;
		@Nullable
		private Integer minZoom = null;
		@Nullable
		private Integer maxZoom = null;

		private final SortedSet<Integer> floorIds = Sets.newTreeSet();
		private final MapDomainFactory mapDomainFactory;

		// CONSTRUCTOR
		@Inject
		public DefaultContinentBuilder(final MapDomainFactory mapDomainFactory) {
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
		public DefaultContinentBuilder dimension(@Nullable final Tuple2<Integer, Integer> dimension) {
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
		public ContinentBuilder floorIds(final Set<Integer> floorIds) {
			this.floorIds.clear();
			if (floorIds != null) {
				this.floorIds.addAll(floorIds);
			}
			return this;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("dimension", this.dimension).add("floorIds", this.floorIds)
					.add("minZoom", this.minZoom).add("maxZoom", this.maxZoom).toString();
		}

	}

	// FIELDS
	private final String id;
	private final String name;
	private final Tuple2<Integer, Integer> dimension;
	private final int minZoom;
	private final int maxZoom;
	private final Iterable<MapFloor> floors;
	private final SortedSet<Integer> floorIds;
	private final LoadingCache<Integer, MapFloor> floorCache = CacheBuilder.newBuilder().build(new CacheLoader<Integer, MapFloor>() {

		@Override
		public MapFloor load(final Integer floorIndex) throws Exception {
			checkNotNull(floorIndex, "missing floorIndex");
			return DefaultContinent.this.mapDomainFactory.newMapFloorBuilder().continentId(DefaultContinent.this.id).floorIndex(floorIndex).minZoom(DefaultContinent.this.minZoom)
					.maxZoom(DefaultContinent.this.maxZoom).build();
		}

	});
	private final MapDomainFactory mapDomainFactory;

	// CONSTRUCTOR
	private DefaultContinent(final DefaultContinentBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.id = checkNotNull(builder.id, "missing id in %s", builder);
		this.name = checkNotNull(builder.name, "missing name in %s", builder);
		this.dimension = checkNotNull(builder.dimension, "missing dimension in %s", builder);
		this.minZoom = checkNotNull(builder.minZoom, "missing minZoom in %s", builder);
		this.maxZoom = checkNotNull(builder.maxZoom, "missing maxZoom in %s", builder);
		this.floorIds = checkNotNull(builder.floorIds, "missing mapFloors in %s", builder);

		this.mapDomainFactory = checkNotNull(builder.mapDomainFactory, "missing mapDomainFactory in %s", builder);

		this.floors = FluentIterable.from(this.getFloorIds()).transform((floorIndex) -> {
			checkNotNull(floorIndex, "missing floorIndex");
			return DefaultContinent.this.getFloor(floorIndex);
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
	public Tuple2<Integer, Integer> getDimension() {
		return this.dimension;
	}

	@Override
	public MapFloor getFloor(final int floorIndex) {
		checkArgument(this.floorIds.contains(floorIndex), "invalid floor index=%s for %s", floorIndex, this);
		try {
			return this.floorCache.get(floorIndex);
		} catch (ExecutionException e) {
			throw new Error(e);
		}
	}

	@Override
	public Iterable<MapFloor> getFloors() {
		return this.floors;
	}

	@Override
	public SortedSet<Integer> getFloorIds() {
		return this.floorIds;
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
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("dimension", this.dimension).add("minZoom", this.minZoom)
				.add("maxZoom", this.maxZoom).add("floorIds", this.floorIds).toString();
	}

}
