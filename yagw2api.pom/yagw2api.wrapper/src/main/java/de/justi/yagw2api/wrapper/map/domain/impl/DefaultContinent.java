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

import de.justi.yagw2api.wrapper.map.domain.Continent;
import de.justi.yagw2api.wrapper.map.domain.ContinentMap;
import de.justi.yagwapi.common.Tuple2;

final class DefaultContinent implements Continent {

	// STATIC
	public static ContinentBuilder builder() {
		return new DefaultContinentBuilder();
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
		private ContinentMap map = null;

		// CONSTRUCTOR
		private DefaultContinentBuilder() {
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
		public DefaultContinentBuilder map(@Nullable final ContinentMap map) {
			this.map = map;
			return this;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("dimension", this.dimension).add("map", this.map).toString();
		}

	}

	// FIELDS
	private final String id;
	private final String name;
	private final Tuple2<Integer, Integer> dimension;
	private final ContinentMap map;

	// CONSTRUCTOR
	private DefaultContinent(final DefaultContinentBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.id = checkNotNull(builder.id, "missing id in %s", builder);
		this.name = checkNotNull(builder.name, "missing name in %s", builder);
		this.dimension = checkNotNull(builder.dimension, "missing dimension in %s", builder);
		this.map = checkNotNull(builder.map, "missing map in %s", this.map);
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
	public ContinentMap getMap() {
		return this.map;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("dimension", this.dimension).add("map", this.map).toString();
	}
}
