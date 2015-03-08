package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.IMapMapDTO;
import de.justi.yagwapi.common.Tuple4;
import de.justi.yagwapi.common.Tuples;

final class MapMapDTO implements IMapMapDTO {
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
	private final Integer[][] bounds = new Integer[2][2];
	@SerializedName("continent_rect")
	@Since(1.0)
	private final Integer[][] boundsOnContinent = new Integer[2][2];

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
				.add("bounds", this.getBounds()).add("boundsOnContinent", this.getBoundsOnContinent()).toString();
	}
}
