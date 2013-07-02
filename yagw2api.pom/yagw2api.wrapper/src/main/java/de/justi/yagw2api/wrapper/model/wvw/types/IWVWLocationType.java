package de.justi.yagw2api.wrapper.model.wvw.types;


import java.util.Locale;

import com.google.common.base.Optional;

import de.justi.yagwapi.common.IImmutable;

public interface IWVWLocationType extends IImmutable{
	Optional<String> getLabel(Locale locale);
	Optional<Integer> getObjectiveId();
	Optional<IWVWObjectiveType> getObjectiveType();
	boolean isObjectiveLocation();
	IWVWMapType getMapType();
}
