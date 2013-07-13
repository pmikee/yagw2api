package de.justi.yagw2api.arenanet;

import java.util.Date;
import java.util.Locale;

import com.google.common.base.Optional;

public interface IWVWMatchDTO {
	int getRedWorldId();
	int getGreenWorldId();
	int getBlueWorldId();
	String getId();
	
	Date getStartTime();
	Date getEndTime();

	Optional<IWorldNameDTO> getRedWorldName(Locale locale);
	Optional<IWorldNameDTO> getGreenWorldName(Locale locale);
	Optional<IWorldNameDTO> getBlueWorldName(Locale locale);
	Optional<IWVWMatchDetailsDTO> getDetails();
}
