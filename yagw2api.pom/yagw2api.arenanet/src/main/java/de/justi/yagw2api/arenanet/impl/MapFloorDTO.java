package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkState;
import scala.Tuple2;
import scala.Tuple4;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.IMapFloorDTO;

final class MapFloorDTO implements IMapFloorDTO {

	@SerializedName("texture_dims")
	@Since(1.0)
	private final Integer[] textureDimension = new Integer[2];
	@SerializedName("clamped_view")
	@Since(1.0)
	private final Integer[][] clampedView = new Integer[2][2];

	private final transient Supplier<Tuple2<Integer, Integer>> textureDimensionTupleSupplier = Suppliers.memoize(() -> {
		checkState(this.textureDimension.length == 2, "invalid texture dimension length: %s", this.textureDimension.length);
		return new Tuple2<Integer, Integer>(this.textureDimension[0], this.textureDimension[1]);
	});

	private final transient Supplier<Optional<Tuple4<Integer, Integer, Integer, Integer>>> clampedViewTupleSupplier = Suppliers.memoize(() -> {
		checkState(this.clampedView.length == 2, "invalid clamped view length: %s", this.clampedView.length);
		checkState(this.clampedView[0].length == 2, "invalid clamped view length: %s", this.clampedView[0].length);
		checkState(this.clampedView[1].length == 2, "invalid clamped view length: %s", this.clampedView[1].length);
		if (this.clampedView[0][0] == null || this.clampedView[0][1] == null || this.clampedView[1][0] == null || this.clampedView[1][1] == null) {
			return Optional.absent();
		} else {
			return Optional.of(new Tuple4<Integer, Integer, Integer, Integer>(this.clampedView[0][0], this.clampedView[0][1], this.clampedView[1][0], this.clampedView[1][1]));
		}
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
		return MoreObjects.toStringHelper(this).add("textureDimension", this.getTextureDimension()).add("clampedView", this.getClampedView()).toString();
	}

}
