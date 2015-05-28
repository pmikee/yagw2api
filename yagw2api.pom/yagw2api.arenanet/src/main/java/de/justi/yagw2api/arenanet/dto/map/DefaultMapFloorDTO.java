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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagwapi.common.tuple.NumberTuple2;
import de.justi.yagwapi.common.tuple.NumberTuple4;
import de.justi.yagwapi.common.tuple.Tuples;

final class DefaultMapFloorDTO implements MapFloorDTO {
	// FIELDS
	@SerializedName("texture_dims")
	@Since(1.0)
	private final int[] textureDimension;
	@SerializedName("clamped_view")
	@Since(1.0)
	@Nullable
	private final int[][] clampedView;

	@SerializedName("regions")
	@Since(1.0)
	private final Map<String, DefaultMapRegionDTO> regions = ImmutableMap.of();

	private final transient Supplier<NumberTuple2<Integer, Integer>> textureDimensionTupleSupplier = Suppliers.memoize(new Supplier<NumberTuple2<Integer, Integer>>() {
		@Override
		public NumberTuple2<Integer, Integer> get() {
			checkNotNull(DefaultMapFloorDTO.this.textureDimension, "missing textureDimension in %s", DefaultMapFloorDTO.this);
			checkState(DefaultMapFloorDTO.this.textureDimension.length == 2, "invalid texture dimension length: %s", DefaultMapFloorDTO.this.textureDimension.length);
			return Tuples.of(DefaultMapFloorDTO.this.textureDimension[0], DefaultMapFloorDTO.this.textureDimension[1]);
		}
	});

	private final transient Supplier<Optional<NumberTuple4<Integer, Integer, Integer, Integer>>> clampedViewTupleSupplier = Suppliers
			.memoize(new Supplier<Optional<NumberTuple4<Integer, Integer, Integer, Integer>>>() {
				@Override
				public Optional<NumberTuple4<Integer, Integer, Integer, Integer>> get() {
					if (DefaultMapFloorDTO.this.clampedView == null) {
						return Optional.absent();
					} else {
						checkNotNull(DefaultMapFloorDTO.this.clampedView, "missing clampedView in %s", DefaultMapFloorDTO.this);
						checkState(DefaultMapFloorDTO.this.clampedView.length == 2, "invalid clamped view length: %s", DefaultMapFloorDTO.this.clampedView.length);
						checkState(DefaultMapFloorDTO.this.clampedView[0].length == 2, "invalid clamped view length: %s", DefaultMapFloorDTO.this.clampedView[0].length);
						checkState(DefaultMapFloorDTO.this.clampedView[1].length == 2, "invalid clamped view length: %s", DefaultMapFloorDTO.this.clampedView[1].length);
						return Optional.of(Tuples.of(DefaultMapFloorDTO.this.clampedView[0][0], DefaultMapFloorDTO.this.clampedView[0][1],
								DefaultMapFloorDTO.this.clampedView[1][0], DefaultMapFloorDTO.this.clampedView[1][1]));
					}
				}
			});

	// CONSTRUCTOR
	DefaultMapFloorDTO() {
		this.clampedView = null;
		this.textureDimension = new int[2];
	}

	DefaultMapFloorDTO(final int[] textureDimension, final int[][] clampedView) {
		checkNotNull(textureDimension, "missing textureDimension");
		checkArgument(textureDimension.length == 2, "unexpected length of textureDimension: %s", textureDimension.length);
		checkNotNull(clampedView, "missing clampedView");
		checkArgument(clampedView.length == 2, "unexpected length of clampedView: %s", clampedView.length);
		checkArgument(clampedView[0].length == 2, "unexpected length of clampedView[0]: %s", clampedView[0].length);
		checkArgument(clampedView[1].length == 2, "unexpected length of clampedView[1]: %s", clampedView[1].length);
		this.textureDimension = textureDimension;
		this.clampedView = clampedView;
	}

	// METHODS
	@Override
	public NumberTuple2<Integer, Integer> getTextureDimension() {
		return this.textureDimensionTupleSupplier.get();
	}

	@Override
	public Optional<NumberTuple4<Integer, Integer, Integer, Integer>> getClampedView() {
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
