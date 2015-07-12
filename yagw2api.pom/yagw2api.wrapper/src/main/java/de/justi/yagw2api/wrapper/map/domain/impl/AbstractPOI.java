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
import com.google.common.base.MoreObjects.ToStringHelper;

import de.justi.yagw2api.common.tuple.DoubleTuple2;
import de.justi.yagw2api.common.tuple.UniformNumberTuple2;
import de.justi.yagw2api.wrapper.map.domain.POI;

abstract class AbstractPOI implements POI {
	// STATICS

	// EMBEDDED
	public static abstract class AbstractPOIBuilder<T extends POI, B extends POI.POIBuilder<T, B>> implements POIBuilder<T, B> {
		// FIELDS
		@Nullable
		private String id = null;
		@Nullable
		private String name = null;
		@Nullable
		private UniformNumberTuple2<Double> location = null;

		// METHODS
		@Override
		public final B id(final String id) {
			this.id = id;
			return this.self();
		}

		@Override
		public final B name(@Nullable final String name) {
			this.name = name;
			return this.self();
		}

		@Override
		public final B location(@Nullable final UniformNumberTuple2<Double> location) {
			this.location = location;
			return this.self();
		}

		protected abstract B self();

		protected ToStringHelper toStringHelper() {
			return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("location", this.location);
		}

		@Override
		public final String toString() {
			return this.toStringHelper().toString();
		}

	}

	// FIELDS
	private final String id;
	private final String name;
	private final DoubleTuple2 location;

	// CONSTRUCTOR
	protected AbstractPOI(final AbstractPOIBuilder<?, ?> builder) {
		checkNotNull(builder, "missing builder");
		this.id = checkNotNull(builder.id, "missing id in %s", builder);
		this.name = checkNotNull(builder.name, "missing name in %s", builder);
		this.location = checkNotNull(builder.location, "missing location in %s", builder).asDoubleTuple2();
	}

	@Override
	public final String getId() {
		return this.id;
	}

	@Override
	public final String getName() {
		return this.name;
	}

	@Override
	public final DoubleTuple2 getLocation() {
		return this.location;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.location == null) ? 0 : this.location.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractPOI)) {
			return false;
		}
		AbstractPOI other = (AbstractPOI) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!this.location.equals(other.location)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	protected ToStringHelper toStringHelper() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("location", this.location);
	}

	@Override
	public final String toString() {
		return this.toStringHelper().toString();
	}
}
