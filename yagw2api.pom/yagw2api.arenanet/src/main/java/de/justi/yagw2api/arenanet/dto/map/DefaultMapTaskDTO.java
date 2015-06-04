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

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagwapi.common.tuple.Tuples;
import de.justi.yagwapi.common.tuple.UniformNumberTuple2;

final class DefaultMapTaskDTO implements MapTaskDTO {
	@SerializedName("task_id")
	@Since(1.0)
	private final Integer id = null;
	@SerializedName("name")
	@Since(1.0)
	private final String name = null;
	@SerializedName("level")
	@Since(1.0)
	private final Integer level = null;
	@SerializedName("coord")
	@Since(1.0)
	private final Double[] coordinates = new Double[2];

	private final transient Supplier<UniformNumberTuple2<Double>> coordinatesTupleSupplier = Suppliers.memoize(() -> {
		checkState(this.coordinates.length == 2, "invalid coordinates length: %s", this.coordinates.length);
		return Tuples.uniformOf(this.coordinates[0], this.coordinates[1]);
	});

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public UniformNumberTuple2<Double> getCoordinates() {
		return this.coordinatesTupleSupplier.get();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("level", this.level).add("coordinates", this.getCoordinates()).toString();
	}
}
