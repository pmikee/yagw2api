package de.justi.gw2.model.wvw.types;


import com.google.common.base.Optional;

import de.justi.gw2.model.wvw.IWVWMapType;

public interface IWVWLocationType {
	String getLabel();
	Optional<Integer> getObjectiveId();
	Optional<IWVWObjectiveType> getObjectiveType();
	boolean isObjectiveLocation();
	IWVWMapType getMapType();
}
