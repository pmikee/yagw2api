package de.justi.yagw2api.core.arenanet.dto;

import java.util.Locale;

import com.google.common.base.Optional;

public interface IWVWMatchDTO {
	int getRedWorldId();
	int getGreenWorldId();
	int getBlueWorldId();
	String getId();

	Optional<IWorldNameDTO> getRedWorldName(Locale locale);
	Optional<IWorldNameDTO> getGreenWorldName(Locale locale);
	Optional<IWorldNameDTO> getBlueWorldName(Locale locale);
	Optional<IWVWMatchDetailsDTO> getDetails();
}
