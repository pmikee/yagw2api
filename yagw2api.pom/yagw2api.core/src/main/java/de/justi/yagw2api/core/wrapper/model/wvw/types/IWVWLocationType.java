package de.justi.yagw2api.core.wrapper.model.wvw.types;


import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.IImmutable;

public interface IWVWLocationType extends IImmutable{
	String getLabel();
	Optional<Integer> getObjectiveId();
	Optional<IWVWObjectiveType> getObjectiveType();
	boolean isObjectiveLocation();
	IWVWMapType getMapType();
}
