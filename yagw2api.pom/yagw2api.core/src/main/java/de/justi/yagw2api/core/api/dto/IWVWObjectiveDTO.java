package de.justi.yagw2api.core.api.dto;

import java.util.Locale;

import com.google.common.base.Optional;

public interface IWVWObjectiveDTO {
	public static final String OWNER_RED_STRING = "RED";
	public static final String OWNER_GREEN_STRING = "GREEN";
	public static final String OWNER_BLUE_STRING = "BLUE";
	int getId();
	Optional<IWVWObjectiveNameDTO> getName(Locale locale); 
	String getOwner();
	String getGuildId();
}
