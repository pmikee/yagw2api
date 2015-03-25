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

import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagwapi.common.Tuple4;
import de.justi.yagwapi.common.Tuples;

final class DefaultMapsMapDTO implements MapsMapDTO {
	@SerializedName("map_name")
	@Since(1.0)
	private final String name = null;
	@SerializedName("min_level")
	@Since(1.0)
	private final String minLevel = null;
	@SerializedName("max_level")
	@Since(1.0)
	private final String maxLevel = null;
	@SerializedName("default_floor")
	@Since(1.0)
	private final String defaultFloor = null;
	@SerializedName("floors")
	@Since(1.0)
	private final Collection<Integer> floors = ImmutableList.of();
	@SerializedName("region_id")
	@Since(1.0)
	private final String regionId = null;
	@SerializedName("region_name")
	@Since(1.0)
	private final String regionName = null;
	@SerializedName("continent_id")
	@Since(1.0)
	private final String continentId = null;
	@SerializedName("continent_name")
	@Since(1.0)
	private final String continentName = null;
	@SerializedName("map_rect")
	@Since(1.0)
	private final int[][] bounds = new int[2][2];
	@SerializedName("continent_rect")
	@Since(1.0)
	private final int[][] boundsOnContinent = new int[2][2];

	private final transient Supplier<Tuple4<Integer, Integer, Integer, Integer>> boundsTupleSupplier = Suppliers.memoize(() -> {
		checkState(this.bounds.length == 2, "invalid bounds length: %s", this.bounds.length);
		checkState(this.bounds[0].length == 2, "invalid bounds length: %s", this.bounds[0].length);
		checkState(this.bounds[1].length == 2, "invalid bounds length: %s", this.bounds[1].length);
		return Tuples.of(this.bounds[0][0], this.bounds[0][1], this.bounds[1][0], this.bounds[1][1]);
	});
	private final transient Supplier<Tuple4<Integer, Integer, Integer, Integer>> boundsOnContinentTupleSupplier = Suppliers.memoize(() -> {
		checkState(this.boundsOnContinent.length == 2, "invalid boundsOnContinent length: %s", this.boundsOnContinent.length);
		checkState(this.boundsOnContinent[0].length == 2, "invalid boundsOnContinent length: %s", this.boundsOnContinent[0].length);
		checkState(this.boundsOnContinent[1].length == 2, "invalid boundsOnContinent length: %s", this.boundsOnContinent[1].length);
		return Tuples.of(this.boundsOnContinent[0][0], this.bounds[0][1], this.boundsOnContinent[1][0], this.boundsOnContinent[1][1]);
	});

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getMinLevel() {
		return this.minLevel;
	}

	@Override
	public String getMaxLevel() {
		return this.maxLevel;
	}

	@Override
	public String getDefaultFloor() {
		return this.defaultFloor;
	}

	@Override
	public Collection<Integer> getFloors() {
		return this.floors;
	}

	@Override
	public String getRegionId() {
		return this.regionId;
	}

	@Override
	public String getRegionName() {
		return this.regionName;
	}

	@Override
	public String getContinentId() {
		return this.continentId;
	}

	@Override
	public String getContinentName() {
		return this.continentName;
	}

	@Override
	public Tuple4<Integer, Integer, Integer, Integer> getBounds() {
		return this.boundsTupleSupplier.get();
	}

	@Override
	public Tuple4<Integer, Integer, Integer, Integer> getBoundsOnContinent() {
		return this.boundsOnContinentTupleSupplier.get();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("name", this.name).add("minLevel", this.minLevel).add("maxLevel", this.maxLevel).add("defaultFloor", this.defaultFloor)
				.add("floors", this.floors).add("bounds", this.getBounds()).add("boundsOnContinent", this.getBoundsOnContinent()).add("continentId", this.continentId)
				.add("continentName", this.continentName).add("regionId", this.regionId).add("regionName", this.regionName).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.bounds);
		result = prime * result + Arrays.hashCode(this.boundsOnContinent);
		result = prime * result + ((this.continentId == null) ? 0 : this.continentId.hashCode());
		result = prime * result + ((this.continentName == null) ? 0 : this.continentName.hashCode());
		result = prime * result + ((this.defaultFloor == null) ? 0 : this.defaultFloor.hashCode());
		result = prime * result + ((this.floors == null) ? 0 : this.floors.hashCode());
		result = prime * result + ((this.maxLevel == null) ? 0 : this.maxLevel.hashCode());
		result = prime * result + ((this.minLevel == null) ? 0 : this.minLevel.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.regionId == null) ? 0 : this.regionId.hashCode());
		result = prime * result + ((this.regionName == null) ? 0 : this.regionName.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DefaultMapsMapDTO))
			return false;
		DefaultMapsMapDTO other = (DefaultMapsMapDTO) obj;
		if (!Arrays.deepEquals(this.bounds, other.bounds))
			return false;
		if (!Arrays.deepEquals(this.boundsOnContinent, other.boundsOnContinent))
			return false;
		if (this.continentId == null) {
			if (other.continentId != null)
				return false;
		} else if (!this.continentId.equals(other.continentId))
			return false;
		if (this.continentName == null) {
			if (other.continentName != null)
				return false;
		} else if (!this.continentName.equals(other.continentName))
			return false;
		if (this.defaultFloor == null) {
			if (other.defaultFloor != null)
				return false;
		} else if (!this.defaultFloor.equals(other.defaultFloor))
			return false;
		if (this.floors == null) {
			if (other.floors != null)
				return false;
		} else if (!this.floors.equals(other.floors))
			return false;
		if (this.maxLevel == null) {
			if (other.maxLevel != null)
				return false;
		} else if (!this.maxLevel.equals(other.maxLevel))
			return false;
		if (this.minLevel == null) {
			if (other.minLevel != null)
				return false;
		} else if (!this.minLevel.equals(other.minLevel))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.regionId == null) {
			if (other.regionId != null)
				return false;
		} else if (!this.regionId.equals(other.regionId))
			return false;
		if (this.regionName == null) {
			if (other.regionName != null)
				return false;
		} else if (!this.regionName.equals(other.regionName))
			return false;
		return true;
	}

}
