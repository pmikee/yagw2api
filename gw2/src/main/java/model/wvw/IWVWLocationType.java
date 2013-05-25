package model.wvw;

import com.google.common.base.Optional;

public interface IWVWLocationType {
	String getLabel();
	Optional<Integer> getObjectiveId();
	Optional<IWVWObjectiveType> getObjectiveType();
	IWVWMapType getMapType();
}
