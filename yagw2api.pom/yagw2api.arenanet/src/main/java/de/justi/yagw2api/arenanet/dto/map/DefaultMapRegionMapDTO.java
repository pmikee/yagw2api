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
import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagwapi.common.tuple.Tuples;
import de.justi.yagwapi.common.tuple.UniformNumberTuple4;

final class DefaultMapRegionMapDTO implements MapRegionMapDTO {
	// FIELDS
	@SerializedName("name")
	@Since(1.0)
	private final String name = null;
	@SerializedName("min_level")
	@Since(1.0)
	private final int minLevel = -1;
	@SerializedName("max_level")
	@Since(1.0)
	private final int maxLevel = -1;
	@SerializedName("default_floor")
	@Since(1.0)
	private final int defaultFloor = -1;
	@SerializedName("map_rect")
	@Since(1.0)
	private final int[][] bounds = new int[2][2];
	@SerializedName("continent_rect")
	@Since(1.0)
	private final int[][] boundsOnContinent = new int[2][2];
	@SerializedName("points_of_interest")
	@Since(1.0)
	private final List<DefaultMapPOIDTO> pois = ImmutableList.of();
	@SerializedName("sectors")
	@Since(1.0)
	private final List<DefaultMapSectorDTO> sectors = ImmutableList.of();
	@SerializedName("skill_challenges")
	@Since(1.0)
	private final List<DefaultMapSkillChallangeDTO> skillChallanges = ImmutableList.of();
	@SerializedName("tasks")
	@Since(1.0)
	private final List<DefaultMapTaskDTO> tasks = ImmutableList.of();

	private final transient Supplier<UniformNumberTuple4<Integer>> boundsTupleSupplier = Suppliers.memoize(() -> {
		checkState(this.bounds.length == 2, "invalid bounds length: %s", this.bounds.length);
		checkState(this.bounds[0].length == 2, "invalid bounds length: %s", this.bounds[0].length);
		checkState(this.bounds[1].length == 2, "invalid bounds length: %s", this.bounds[1].length);
		return Tuples.uniformOf(this.bounds[0][0], this.bounds[0][1], this.bounds[1][0], this.bounds[1][1]);
	});
	private final transient Supplier<UniformNumberTuple4<Integer>> boundsOnContinentTupleSupplier = Suppliers.memoize(() -> {
		checkState(this.boundsOnContinent.length == 2, "invalid boundsOnContinent length: %s", this.boundsOnContinent.length);
		checkState(this.boundsOnContinent[0].length == 2, "invalid boundsOnContinent length: %s", this.boundsOnContinent[0].length);
		checkState(this.boundsOnContinent[1].length == 2, "invalid boundsOnContinent length: %s", this.boundsOnContinent[1].length);
		return Tuples.uniformOf(this.boundsOnContinent[0][0], this.boundsOnContinent[0][1], this.boundsOnContinent[1][0], this.boundsOnContinent[1][1]);
	});

	// CONSTRUCTOR

	// METHODS
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getMinLevel() {
		return this.minLevel;
	}

	@Override
	public int getMaxLevel() {
		return this.maxLevel;
	}

	@Override
	public int getDefaultFloor() {
		return this.defaultFloor;
	}

	@Override
	public UniformNumberTuple4<Integer> getBounds() {
		return this.boundsTupleSupplier.get();
	}

	@Override
	public UniformNumberTuple4<Integer> getBoundsOnContinent() {
		return this.boundsOnContinentTupleSupplier.get();
	}

	@Override
	public List<MapPOIDTO> getPOIs() {
		return Collections.unmodifiableList(this.pois);
	}

	@Override
	public List<MapSectorDTO> getSectors() {
		return Collections.unmodifiableList(this.sectors);
	}

	@Override
	public List<MapSkillChallangeDTO> getSkillChallanges() {
		return Collections.unmodifiableList(this.skillChallanges);
	}

	@Override
	public List<MapTaskDTO> getTasks() {
		return Collections.unmodifiableList(this.tasks);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("name", this.name).add("minLevel", this.minLevel).add("maxLevel", this.maxLevel).add("defaultFloor", this.defaultFloor)
				.add("bounds", this.getBounds()).add("boundsOnContinent", this.getBoundsOnContinent()).add("pois", this.pois).add("sectors", this.sectors)
				.add("skillChallanges", this.skillChallanges).add("tasks", this.tasks).toString();
	}
}
