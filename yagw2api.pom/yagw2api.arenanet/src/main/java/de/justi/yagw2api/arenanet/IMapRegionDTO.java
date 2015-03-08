package de.justi.yagw2api.arenanet;

import java.util.Map;

import de.justi.yagwapi.common.Tuple2;

public interface IMapRegionDTO {
	String getName();

	Tuple2<Integer, Integer> getLabelCoordinates();

	Map<String, IMapMapDTO> getMaps();
}
