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

import de.justi.yagw2api.wrapper.map.domain.MapFloor;

final class DefaultMapFloor implements MapFloor {
	// STATICS
	public static MapFloorBuilder builder() {
		return new DefaultMapFloorBuilder();
	}

	// EMBEDDED
	private static final class DefaultMapFloorBuilder implements MapFloor.MapFloorBuilder {
		// FIELDS
		@Nullable
		private Integer id = null;

		// CONSTRUCTOR
		private DefaultMapFloorBuilder() {
		}

		// METHODS
		@Override
		public MapFloor build() {
			return new DefaultMapFloor(this);
		}

		@Override
		public MapFloorBuilder index(final int index) {
			this.id = index;
			return this;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("id", this.id).toString();
		}
	}

	// FIELDS
	private final int id;

	// CONSTRUCTOR
	private DefaultMapFloor(final DefaultMapFloorBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.id = checkNotNull(builder.id, "missing id in %s", builder);
	}

	// METHODS
	@Override
	public int getIndex() {
		return this.id;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).addValue(this.id).toString();
	}
}
