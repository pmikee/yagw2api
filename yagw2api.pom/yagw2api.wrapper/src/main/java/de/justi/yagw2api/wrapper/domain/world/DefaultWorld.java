package de.justi.yagw2api.wrapper.domain.world;

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

import java.util.Locale;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.dto.world.WorldNameDTO;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.domain.ModelFactory;
import de.justi.yagwapi.common.Unmodifiable;

public final class DefaultWorld implements World {
	private static final ModelFactory MODEL_FACTORY = YAGW2APIWrapper.INSTANCE.getModelFactory();

	public static final class DefaultWorldBuilder implements World.WorldBuilder {
		private Optional<Integer> id = Optional.absent();
		private Optional<Locale> locale = Optional.absent();
		private Optional<WorldLocationType> location = Optional.absent();
		private Optional<String> name = Optional.absent();

		@Override
		public World build() {
			checkState(this.location.isPresent());
			checkState(this.id.isPresent());
			checkState(this.id.get() > 0);
			final Optional<World> worldOptional = MODEL_FACTORY.getWorld(this.id.get());
			checkState(!worldOptional.isPresent() || (worldOptional.get() instanceof DefaultWorld));
			final DefaultWorld world;
			if (worldOptional.isPresent()) {
				world = (DefaultWorld) worldOptional.get();
				checkState(world.getId() == this.id.get());
				checkState(((world.getWorldLocale() == null) && !this.locale.isPresent()) || world.getWorldLocale().equals(this.locale.orNull()));
				checkState(world.getWorldLocation().equals(this.location.get()));
			} else {
				world = new DefaultWorld(this.id.get(), this.locale.orNull(), this.location.get());
			}
			if (this.name.isPresent()) {
				world.setName(this.name.get());
			}
			return world;
		}

		@Override
		public WorldBuilder fromDTO(final WorldNameDTO dto) {
			checkArgument(dto.isEurope() ^ dto.isNorthAmerica());
			if (dto.isEurope()) {
				this.worldLocation(DefaultWorldLocationType.EUROPE);
			} else if (dto.isNorthAmerica()) {
				this.worldLocation(DefaultWorldLocationType.NORTH_AMERICA);
			}
			this.name(dto.getNameWithoutLocale());
			this.worldLocale(dto.getWorldLocale().orNull());
			this.id(dto.getId());
			return this;
		}

		@Override
		public WorldBuilder worldLocation(final WorldLocationType location) {
			this.location = Optional.fromNullable(location);
			return this;
		}

		@Override
		public WorldBuilder name(final String name) {
			this.name = Optional.fromNullable(name);
			return this;
		}

		@Override
		public WorldBuilder id(final int id) {
			checkArgument(id > 0);
			this.id = Optional.of(id);
			return this;
		}

		@Override
		public WorldBuilder worldLocale(final Locale locale) {
			this.locale = Optional.fromNullable(locale);
			return this;
		}

	}

	class UnmodifiableWorld implements Unmodifiable, World {

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
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + Unmodifiable.class.getSimpleName()
					+ " and therefore can not be modified.");
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

	public DefaultWorld(final int id, final Locale locale, final WorldLocationType location) {
		checkArgument(id > 0);
		this.id = id;
		this.locale = Optional.fromNullable(locale);
		this.location = location;
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
