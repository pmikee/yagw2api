package model.wvw.types;

import model.wvw.IWVWMapType;

import com.google.common.base.Optional;

public interface IWVWLocationType {
	String getLabel();
	Optional<Integer> getObjectiveId();
	Optional<IWVWObjectiveType> getObjectiveType();
	boolean isObjectiveLocation();
	IWVWMapType getMapType();
}
