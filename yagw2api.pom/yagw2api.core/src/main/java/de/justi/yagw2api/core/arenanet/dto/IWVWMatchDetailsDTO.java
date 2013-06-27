package de.justi.yagw2api.core.arenanet.dto;

import com.google.common.base.Optional;

public interface IWVWMatchDetailsDTO {

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
