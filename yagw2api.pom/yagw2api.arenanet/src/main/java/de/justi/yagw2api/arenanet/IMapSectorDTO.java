package de.justi.yagw2api.arenanet;

import de.justi.yagwapi.common.Tuple2;

public interface IMapSectorDTO {
	int getId();

	String getName();

	int getFloor();

	Tuple2<Double, Double> getCoordinates();
}
