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

import static com.google.common.base.Preconditions.checkState;

import java.util.Collections;
import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagwapi.common.Tuple2;
import de.justi.yagwapi.common.Tuples;

final class DefaultMapRegionDTO implements MapRegionDTO {
	// FIELDS
	@SerializedName("name")
	@Since(1.0)
	private final String name = null;
	@SerializedName("label_coord")
	@Since(1.0)
	private final Integer[] labelCoordinates = new Integer[2];
	@SerializedName("maps")
	@Since(1.0)
	private final Map<String, DefaultMapRegionMapDTO> maps = ImmutableMap.of();

	private final transient Supplier<Tuple2<Integer, Integer>> labelCoordinatesTupleSupplier = Suppliers.memoize(() -> {
		checkState(this.labelCoordinates.length == 2, "invalid texture dimension length: %s", this.labelCoordinates.length);
		return Tuples.of(this.labelCoordinates[0], this.labelCoordinates[1]);
	});

	// METHODS

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Tuple2<Integer, Integer> getLabelCoordinates() {
		return this.labelCoordinatesTupleSupplier.get();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("name", this.name).add("labelCoordinates", this.getLabelCoordinates()).add("maps", this.maps).toString();
	}

	@Override
	public Map<String, MapRegionMapDTO> getMaps() {
		return Collections.unmodifiableMap(this.maps);
	}

}
