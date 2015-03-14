package de.justi.yagw2api.arenanet;

import com.google.gson.annotations.SerializedName;

import de.justi.yagwapi.common.Tuple2;

public interface IMapPOIDTO {

	enum MapPOIType {
		/**
		 * actual points of interest
		 */
		@SerializedName("landmark")
		LANDMARK,
		@SerializedName("waypoint")
		WAYPOINT,
		@SerializedName("vista")
		VISTA
	}

	int getId();

	String getName();

	MapPOIType getType();

	int getFloor();

	Tuple2<Double, Double> getCoordinates();
}
