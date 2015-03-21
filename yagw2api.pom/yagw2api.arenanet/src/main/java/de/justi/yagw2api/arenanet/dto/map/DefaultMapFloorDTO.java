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
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagwapi.common.Tuple2;
import de.justi.yagwapi.common.Tuple4;
import de.justi.yagwapi.common.Tuples;

final class DefaultMapFloorDTO implements MapFloorDTO {

	@SerializedName("texture_dims")
	@Since(1.0)
	private final int[] textureDimension = new int[2];
	@SerializedName("clamped_view")
	@Since(1.0)
	private final int[][] clampedView = new int[2][2];

	@SerializedName("regions")
	@Since(1.0)
	private final Map<String, DefaultMapRegionDTO> regions = ImmutableMap.of();

	private final transient Supplier<Tuple2<Integer, Integer>> textureDimensionTupleSupplier = Suppliers.memoize(() -> {
		checkState(this.textureDimension.length == 2, "invalid texture dimension length: %s", this.textureDimension.length);
		return Tuples.of(this.textureDimension[0], this.textureDimension[1]);
	});

	private final transient Supplier<Optional<Tuple4<Integer, Integer, Integer, Integer>>> clampedViewTupleSupplier = Suppliers.memoize(() -> {
		checkState(this.clampedView.length == 2, "invalid clamped view length: %s", this.clampedView.length);
		checkState(this.clampedView[0].length == 2, "invalid clamped view length: %s", this.clampedView[0].length);
		checkState(this.clampedView[1].length == 2, "invalid clamped view length: %s", this.clampedView[1].length);
		return Optional.of(Tuples.of(this.clampedView[0][0], this.clampedView[0][1], this.clampedView[1][0], this.clampedView[1][1]));
	});

	@Override
	public Tuple2<Integer, Integer> getTextureDimension() {
		return this.textureDimensionTupleSupplier.get();
	}

	@Override
	public Optional<Tuple4<Integer, Integer, Integer, Integer>> getClampedView() {
		return this.clampedViewTupleSupplier.get();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("textureDimension", this.getTextureDimension()).add("clampedView", this.getClampedView()).add("regions", this.regions)
				.toString();
	}

	@Override
	public Map<String, MapRegionDTO> getRegions() {
		return Collections.unmodifiableMap(this.regions);
	}

}
