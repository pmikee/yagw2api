package de.justi.yagw2api.arenanet;

import de.justi.yagwapi.common.Tuple2;

public interface IMapTaskDTO {
	int getId();

	String getName();

	int getLevel();

	Tuple2<Double, Double> getCoordinates();
}
