package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkState;

import java.util.Collections;
import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.IMapMapDTO;
import de.justi.yagw2api.arenanet.IMapRegionDTO;
import de.justi.yagwapi.common.Tuple2;
import de.justi.yagwapi.common.Tuples;

final class MapRegionDTO implements IMapRegionDTO {
	// FIELDS
	@SerializedName("name")
	@Since(1.0)
	private final String name = null;
	@SerializedName("label_coord")
	@Since(1.0)
	private final Integer[] labelCoordinates = new Integer[2];
	@SerializedName("maps")
	@Since(1.0)
	private final Map<String, MapMapDTO> maps = ImmutableMap.of();

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
	public Map<String, IMapMapDTO> getMaps() {
		return Collections.unmodifiableMap(this.maps);
	}

}
