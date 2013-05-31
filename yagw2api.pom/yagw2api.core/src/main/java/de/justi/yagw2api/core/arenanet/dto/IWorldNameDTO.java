package de.justi.yagw2api.core.arenanet.dto;

import java.util.Locale;

import com.google.common.base.Optional;


public interface IWorldNameDTO {
	int getId();
	boolean isEurope();
	boolean isNorthAmerica();
	String getNameWithoutLocale();
	String getName();
	Optional<Locale> getWorldLocale();
}