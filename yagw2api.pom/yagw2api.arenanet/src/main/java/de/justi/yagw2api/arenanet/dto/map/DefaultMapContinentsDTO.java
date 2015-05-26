package de.justi.yagw2api.arenanet.dto.map;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
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

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

final class DefaultMapContinentsDTO implements MapContinentsDTO {

	// EMBEDDED
	private static abstract class AbstractMapContinentWithId extends DelegatingMapContinent implements MapContinentWithIdDTO {
		protected AbstractMapContinentWithId(final MapContinentDTO delegate) {
			super(checkNotNull(delegate, "missing delegate"));
		}
	}

	private static final Function<Entry<String, ? extends MapContinentDTO>, MapContinentWithIdDTO> CONVERTER = new Function<Map.Entry<String, ? extends MapContinentDTO>, MapContinentWithIdDTO>() {
		@Override
		public MapContinentWithIdDTO apply(final Entry<String, ? extends MapContinentDTO> input) {
			checkNotNull(input, "missing input");
			return new AbstractMapContinentWithId(input.getValue()) {
				@Override
				public String getId() {
					return input.getKey();
				}

				@Override
				public ToStringHelper toStringHelper() {
					return super.toStringHelper().add("id", input.getKey());
				}
			};
		}
	};

	// FIELDS
	@Since(1.0)
	@SerializedName("continents")
	private final Map<String, DefaultMapContinentDTO> continents = ImmutableMap.of();

	// CONSTRUCTOR

	// METHODS

	@Override
	public Iterable<MapContinentWithIdDTO> getContinents() {
		return FluentIterable.from(this.continents.entrySet()).transform(CONVERTER);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("continents", this.continents).toString();
	}
}
