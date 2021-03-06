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

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import de.justi.yagw2api.common.tuple.IntTuple4;
import de.justi.yagw2api.wrapper.map.domain.Map;
import de.justi.yagw2api.wrapper.map.domain.POI;

final class DefaultMap implements Map {
	// STATICS
	public static final DefaultMapBuilder builder() {
		return new DefaultMapBuilder();
	}

	// EMBEDDED
	public static final class DefaultMapBuilder implements Map.MapBuilder {
		// FIELDS
		@Nullable
		private String id = null;
		@Nullable
		private String name = null;
		@Nullable
		private Integer defaultFloorIndex = null;
		@Nullable
		private IntTuple4 locationOnContinent = null;
		@Nullable
		private Supplier<Iterable<POI>> poiSupplier = null;

		// METHODS
		@Override
		public MapBuilder id(final String id) {
			this.id = id;
			return this;
		}

		@Override
		public MapBuilder name(@Nullable final String name) {
			this.name = name;
			return this;
		}

		@Override
		public MapBuilder boundsOnContinent(@Nullable final IntTuple4 locationOnContinent) {
			this.locationOnContinent = locationOnContinent;
			return this;
		}

		@Override
		public MapBuilder defaultFloorIndex(final int floorIndex) {
			this.defaultFloorIndex = floorIndex;
			return this;
		}

		@Override
		public MapBuilder pois(final Supplier<Iterable<POI>> poiSupplier) {
			this.poiSupplier = poiSupplier;
			return this;
		}

		@Override
		public Map build() {
			return new DefaultMap(this);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("defaultFloorIndex", this.defaultFloorIndex)
					.add("locationOnContinent", this.locationOnContinent).add("pois", this.poiSupplier).toString();
		}

	}

	// FIELDS
	private final String id;
	private final String name;
	private final int defaultFloorIndex;
	private final IntTuple4 locationOnContinent;
	private final Supplier<Iterable<POI>> poiSupplier;

	// CONSTRUCTOR
	private DefaultMap(final DefaultMapBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.id = checkNotNull(builder.id, "missing id in %s", builder);
		this.defaultFloorIndex = checkNotNull(builder.defaultFloorIndex, "missing defaultFloorIndex in %s", builder);
		this.name = checkNotNull(builder.name, "missing name in %s", builder);
		this.locationOnContinent = checkNotNull(builder.locationOnContinent, "missing locationOnContinent in %s", builder);
		this.poiSupplier = Suppliers.memoize(checkNotNull(builder.poiSupplier, "missing poiSupplier in %s", builder));
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
	public IntTuple4 getBoundsOnContinent() {
		return this.locationOnContinent;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("defaultFloorIndex", this.defaultFloorIndex)
				.add("locationOnContinent", this.locationOnContinent).add("pois", this.poiSupplier).toString();
	}

	@Override
	public int getDefaultFloorIndex() {
		return this.defaultFloorIndex;
	}

	@Override
	public Iterable<POI> getPOIs() {
		return this.poiSupplier.get();
	}

}
