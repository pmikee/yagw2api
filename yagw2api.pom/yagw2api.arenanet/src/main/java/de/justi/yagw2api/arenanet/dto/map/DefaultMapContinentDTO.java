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
import java.util.Set;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagwapi.common.Tuple2;
import de.justi.yagwapi.common.Tuples;

final class DefaultMapContinentDTO implements MapContinentDTO {

	// FIELDS
	@SerializedName("name")
	@Since(1.0)
	private final String name = null;
	@SerializedName("min_zoom")
	@Since(1.0)
	private final Integer minZoom = null;
	@SerializedName("max_zoom")
	@Since(1.0)
	private final Integer maxZoom = null;
	@SerializedName("floors")
	@Since(1.0)
	private final Set<Integer> floors = ImmutableSet.of();

	@SerializedName("continent_dims")
	@Since(1.0)
	private final Integer[] dimension = new Integer[2];
	private final transient Supplier<Tuple2<Integer, Integer>> dimensionTupleSupplier = Suppliers.memoize(() -> {
		checkState(this.dimension.length == 2, "invalid texture dimension length: %s", this.dimension.length);
		return Tuples.of(this.dimension[0], this.dimension[1]);
	});

	// METHODS

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("name", this.name).add("dimension", this.getDimension()).add("minZoom", this.minZoom).add("maxZoom", this.maxZoom).toString();
	}

	@Override
	public Tuple2<Integer, Integer> getDimension() {
		return this.dimensionTupleSupplier.get();
	}

	@Override
	public int getMinZoom() {
		return this.minZoom;
	}

	@Override
	public int getMaxZoom() {
		return this.maxZoom;
	}

	@Override
	public Set<Integer> getFloors() {
		return Collections.unmodifiableSet(this.floors);
	}

}
