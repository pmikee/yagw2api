package de.justi.yagw2api.core.arenanet.dto;

import com.google.common.base.Optional;

public interface IWVWMatchDetailsDTO {
	public static final String CENTER_MAP_TYPE_STRING = "CENTER";
	public static final String RED_MAP_TYPE_STRING = "REDHOME";
	public static final String GREEN_MAP_TYPE_STRING = "GREENHOME";
	public static final String BLUE_MAP_TYPE_STRING = "BLUEHOME";

	String getMatchID();

	int getRedScore();
	int getGreenScore();
	int getBlueScore();
	

	IWVWMapDTO[] getMaps();

	Optional<IWVWMapDTO> getMapForTypeString(String dtoMapTypeString);

	IWVWMapDTO getCenterMap();

	IWVWMapDTO getRedMap();

	IWVWMapDTO getGreenMap();

	IWVWMapDTO getBlueMap();

	Optional<IWVWMatchDTO> getMatch();
}
