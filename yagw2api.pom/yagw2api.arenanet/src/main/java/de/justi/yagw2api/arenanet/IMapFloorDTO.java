package de.justi.yagw2api.arenanet;

import scala.Tuple2;
import scala.Tuple4;

import com.google.common.base.Optional;

public interface IMapFloorDTO {
	Tuple2<Integer, Integer> getTextureDimension();

	Optional<Tuple4<Integer, Integer, Integer, Integer>> getClampedView();
}