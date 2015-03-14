package de.justi.yagw2api.arenanet;

import java.util.List;

import de.justi.yagwapi.common.Tuple4;

public interface IMapMapDTO {

	String getName();

	int getMinLevel();

	int getMaxLevel();

	int getDefaultFloor();

	/**
	 * @return the dimensions of the map, given as the coordinates of the lower-left (SW) and upper-right (NE) corners.
	 */
	Tuple4<Integer, Integer, Integer, Integer> getBounds();

	/**
	 * 
	 * @return The dimensions of the map within the continent coordinate system, given as the coordinates of the upper-left (NW) and lower-right (SE) corners.
	 */
	Tuple4<Integer, Integer, Integer, Integer> getBoundsOnContinent();

	List<IMapPOIDTO> getPOIs();

	List<IMapSectorDTO> getSectors();

	List<IMapSkillChallangeDTO> getSkillChallanges();

	List<IMapTaskDTO> getTasks();
}
