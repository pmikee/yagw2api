package de.justi.yagw2api.wrapper.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
 * -------------------------------------------------------------
 * Copyright (C) 2012 - 2013 Julian Stitz
 * -------------------------------------------------------------
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Locale;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.IWorldNameDTO;
import de.justi.yagw2api.wrapper.IModelFactory;
import de.justi.yagw2api.wrapper.IWorld;
import de.justi.yagw2api.wrapper.IWorldLocationType;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagwapi.common.IUnmodifiable;

final class World implements IWorld {
	private static final IModelFactory MODEL_FACTORY = YAGW2APIWrapper.INSTANCE.getModelFactory();

	static final class WorldBuilder implements IWorld.IWorldBuilder {
		private Optional<Integer> id = Optional.absent();
		private Optional<Locale> locale = Optional.absent();
		private Optional<IWorldLocationType> location = Optional.absent();
		private Optional<String> name = Optional.absent();

		@Override
		public IWorld build() {
			checkState(this.location.isPresent());
			checkState(this.id.isPresent());
			checkState(this.id.get() > 0);
			final Optional<IWorld> worldOptional = MODEL_FACTORY.getWorld(this.id.get());
			checkState(!worldOptional.isPresent() || (worldOptional.get() instanceof World));
			final World world;
			if (worldOptional.isPresent()) {
				world = (World) worldOptional.get();
				checkState(world.getId() == this.id.get());
				checkState(((world.getWorldLocale() == null) && !this.locale.isPresent()) || world.getWorldLocale().equals(this.locale.orNull()));
				checkState(world.getWorldLocation().equals(this.location.get()));
			} else {
				world = new World(this.id.get(), this.locale.orNull(), this.location.get());
			}
			if (this.name.isPresent()) {
				world.setName(this.name.get());
			}
			return world;
		}

		@Override
		public IWorldBuilder fromDTO(IWorldNameDTO dto) {
			checkArgument(dto.isEurope() ^ dto.isNorthAmerica());
			if (dto.isEurope()) {
				this.worldLocation(WorldLocationType.EUROPE);
			} else if (dto.isNorthAmerica()) {
				this.worldLocation(WorldLocationType.NORTH_AMERICA);
			}
			this.name(dto.getNameWithoutLocale());
			this.worldLocale(dto.getWorldLocale().orNull());
			this.id(dto.getId());
			return this;
		}

		@Override
		public IWorldBuilder worldLocation(IWorldLocationType location) {
			this.location = Optional.fromNullable(location);
			return this;
		}

		@Override
		public IWorldBuilder name(String name) {
			this.name = Optional.fromNullable(name);
			return this;
		}

		@Override
		public IWorldBuilder id(int id) {
			checkArgument(id > 0);
			this.id = Optional.of(id);
			return this;
		}

		@Override
		public IWorldBuilder worldLocale(Locale locale) {
			this.locale = Optional.fromNullable(locale);
			return this;
		}

	}

	class UnmodifiableWorld implements IUnmodifiable, IWorld {

		@Override
		public int getId() {
			return World.this.getId();
		}

		@Override
		public Optional<String> getName() {
			return World.this.getName();
		}

		@Override
		public void setName(String name) {
			throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is instance of " + IUnmodifiable.class.getSimpleName() + " and therefore can not be modified.");
		}

		@Override
		public IWorld createUnmodifiableReference() {
			return this;
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(this).addValue(World.this.toString()).toString();
		}

		@Override
		public int hashCode() {
			return World.this.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return World.this.equals(obj);
		}

		@Override
		public Optional<Locale> getWorldLocale() {
			return World.this.getWorldLocale();
		}

		@Override
		public IWorldLocationType getWorldLocation() {
			return World.this.getWorldLocation();
		}
	}

	private final int id;
	private Optional<String> name = Optional.absent();
	private final Optional<Locale> locale;
	private final IWorldLocationType location;

	public World(int id, Locale locale, IWorldLocationType location) {
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
		return Objects.toStringHelper(this).add("id", this.id).add("name", this.name).add("locale", this.locale).add("location", this.location).toString();
	}

	@Override
	public void setName(String name) {
		checkNotNull(name);
		this.name = Optional.of(name);
	}

	@Override
	public IWorld createUnmodifiableReference() {
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getClass().getName(), this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof IWorld)) {
			return false;
		} else {
			final IWorld world = (IWorld) obj;
			return Objects.equal(this.id, world.getId());
		}
	}

	@Override
	public Optional<Locale> getWorldLocale() {
		return this.locale;
	}

	@Override
	public IWorldLocationType getWorldLocation() {
		return this.location;
	}

}
