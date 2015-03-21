package de.justi.yagw2api.arenanet.dto.map;

import java.util.Collection;

import de.justi.yagwapi.common.Tuple4;

public interface MapsMapDTO {
	String getName();

	String getMinLevel();

	String getMaxLevel();

	String getDefaultFloor();

	Collection<Integer> getFloors();

	String getRegionId();

	String getRegionName();

	String getContinentId();

	String getContinentName();

	/**
	 * @return the dimensions of the map, given as the coordinates of the lower-left (SW) and upper-right (NE) corners.
	 */
	Tuple4<Integer, Integer, Integer, Integer> getBounds();

	/**
	 * 
	 * @return The dimensions of the map within the continent coordinate system, given as the coordinates of the upper-left (NW) and lower-right (SE) corners.
	 */
	Tuple4<Integer, Integer, Integer, Integer> getBoundsOnContinent();

}
