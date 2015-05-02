package de.justi.yagw2api.arenanet.dto.map;

import java.util.Set;

import de.justi.yagwapi.common.Tuple2;

public interface MapContinentDTO {
	String getName();

	Tuple2<Integer, Integer> getDimension();

	int getMinZoom();

	int getMaxZoom();

	Set<Integer> getFloors();
}
