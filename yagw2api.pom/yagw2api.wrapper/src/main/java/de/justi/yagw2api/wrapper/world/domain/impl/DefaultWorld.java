package de.justi.yagw2api.wrapper.world.domain.impl;

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

import java.util.Locale;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.v1.dto.world.WorldNameDTO;
import de.justi.yagw2api.wrapper.world.domain.World;
import de.justi.yagw2api.wrapper.world.domain.WorldLocationType;

public final class DefaultWorld implements World {
	// STATIC
	public static WorldBuilder builder() {
		return new DefaultWorldBuilder();
	}

	// EMBEDDED
	private static final class DefaultWorldBuilder implements World.WorldBuilder {
		// FIELDS
		@Nullable
		private Integer id = null;
		@Nullable
		private Locale locale = null;
		@Nullable
		private WorldLocationType location = null;
		@Nullable
		private String name = null;

		// CONSTRUCTOR
		public DefaultWorldBuilder() {
		}

		// METHODS

		@Override
		public World build() {
			return new DefaultWorld(this);
		}

		@Override
		public WorldBuilder fromDTO(final WorldNameDTO dto) {
			checkArgument(dto.isEurope() ^ dto.isNorthAmerica());
			if (dto.isEurope()) {
				this.worldLocation(DefaultWorldLocationType.EUROPE);
			} else if (dto.isNorthAmerica()) {
				this.worldLocation(DefaultWorldLocationType.NORTH_AMERICA);
			}
			return this.name(dto.getNameWithoutLocale()).worldLocale(dto.getWorldLocale().orNull()).id(dto.getId());
		}

		@Override
		public WorldBuilder worldLocation(@Nullable final WorldLocationType location) {
			this.location = location;
			return this;
		}

		@Override
		public WorldBuilder name(@Nullable final String name) {
			this.name = name;
			return this;
		}

		@Override
		public WorldBuilder id(final int id) {
			this.id = id;
			return this;
		}

		@Override
		public WorldBuilder worldLocale(@Nullable final Locale locale) {
			this.locale = locale;
			return this;
		}
	}

	class UnmodifiableWorld implements World {

		@Override
		public int getId() {
			return DefaultWorld.this.getId();
		}

		@Override
		public Optional<String> getName() {
			return DefaultWorld.this.getName();
		}

		@Override
		public void setName(final String name) {
			throw new UnsupportedOperationException("unmodifiable");
		}

		@Override
		public World createUnmodifiableReference() {
			return this;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).addValue(DefaultWorld.this.toString()).toString();
		}

		@Override
		public int hashCode() {
			return DefaultWorld.this.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			return DefaultWorld.this.equals(obj);
		}

		@Override
		public Optional<Locale> getWorldLocale() {
			return DefaultWorld.this.getWorldLocale();
		}

		@Override
		public WorldLocationType getWorldLocation() {
			return DefaultWorld.this.getWorldLocation();
		}
	}

	private final int id;
	private Optional<String> name = Optional.absent();
	private final Optional<Locale> locale;
	private final WorldLocationType location;

	private DefaultWorld(final DefaultWorldBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.id = checkNotNull(builder.id, "missing id in %s", builder);
		this.location = checkNotNull(builder.location, "missing location in %s", builder);

		this.locale = Optional.fromNullable(builder.locale);
		this.name = Optional.fromNullable(builder.name);
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public Optional<String> getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("locale", this.locale).add("location", this.location).toString();
	}

	@Override
	public void setName(final String name) {
		checkNotNull(name);
		this.name = Optional.of(name);
	}

	@Override
	public World createUnmodifiableReference() {
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getClass().getName(), this.id);
	}

	@Override
	public boolean equals(final Object obj) {
		if ((obj == null) || !(obj instanceof World)) {
			return false;
		} else {
			final World world = (World) obj;
			return Objects.equal(this.id, world.getId());
		}
	}

	@Override
	public Optional<Locale> getWorldLocale() {
		return this.locale;
	}

	@Override
	public WorldLocationType getWorldLocation() {
		return this.location;
	}

}
