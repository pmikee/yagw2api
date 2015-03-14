package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.IMapSectorDTO;
import de.justi.yagwapi.common.Tuple2;
import de.justi.yagwapi.common.Tuples;

final class MapSectorDTO implements IMapSectorDTO {
	@SerializedName("sector_id")
	@Since(1.0)
	private final Integer id = null;
	@SerializedName("name")
	@Since(1.0)
	private final String name = null;
	@SerializedName("floor")
	@Since(1.0)
	private final Integer floor = null;
	@SerializedName("coord")
	@Since(1.0)
	private final Double[] coordinates = new Double[2];

	private final transient Supplier<Tuple2<Double, Double>> coordinatesTupleSupplier = Suppliers.memoize(() -> {
		checkState(this.coordinates.length == 2, "invalid coordinates length: %s", this.coordinates.length);
		return Tuples.of(this.coordinates[0], this.coordinates[1]);
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
	public int getFloor() {
		return this.floor;
	}

	@Override
	public Tuple2<Double, Double> getCoordinates() {
		return this.coordinatesTupleSupplier.get();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("floor", this.floor).add("coordinates", this.getCoordinates()).toString();
	}
}
