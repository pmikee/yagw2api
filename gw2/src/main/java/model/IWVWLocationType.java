package model;

import com.google.common.base.Optional;

public interface IWVWLocationType {
	String getLabel();
	Optional<Integer> getObjectiveId();
	IWVWMapType getMapType();
}
