package de.justi.yagw2api.wrapper.map;

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

import com.google.common.base.Predicate;

import de.justi.yagw2api.wrapper.map.domain.Continent;

public final class MapPredicates {
	// STATICS

	public static Predicate<Continent> continentIdEquals(final String id) {
		checkNotNull(id, "missing id");
		return new Predicate<Continent>() {
			@Override
			public boolean apply(final Continent input) {
				checkNotNull(input, "missing input");
				return input.getId().equals(id);
			}
		};
	}

	// EMBEDDED

	// CONSTRUCTOR
	private MapPredicates() {
		throw new AssertionError("no instance");
	}
}
