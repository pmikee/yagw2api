package de.justi.yagw2api.wrapper.domain.map;

import java.util.List;

import de.justi.yagwapi.common.Tuple2;

public interface ContinentMap {
	int getContinentId();

	Tuple2<Integer, Integer> getPointDimension();

	Tuple2<Integer, Integer> getTileDimension(MapFloor floor);

	MapFloorTiles getFloorTiles(MapFloor floor);

	List<MapFloor> getFloors();
}
