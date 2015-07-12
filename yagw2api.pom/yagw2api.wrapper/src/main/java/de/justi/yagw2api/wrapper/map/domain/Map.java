package de.justi.yagw2api.wrapper.map.domain;

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

import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;

import de.justi.yagw2api.common.tuple.IntTuple4;

public interface Map {

	public static interface MapBuilder {

		MapBuilder id(@Nullable String id);

		MapBuilder name(@Nullable String name);

		MapBuilder defaultFloorIndex(int floorIndex);

		MapBuilder boundsOnContinent(@Nullable IntTuple4 locationOnContinent);

		MapBuilder pois(@Nullable Supplier<Iterable<POI>> poiSupplier);

		Map build();
	}

	int getDefaultFloorIndex();

	String getId();

	String getName();

	IntTuple4 getBoundsOnContinent();

	Iterable<POI> getPOIs();

	default <T extends POI> Iterable<T> getPOIs(final Class<T> clazz) {
		checkNotNull(clazz, "missing clazz");
		return Iterables.filter(this.getPOIs(), clazz);
	}
}
