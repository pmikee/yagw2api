package de.justi.yagw2api.gw2stats.dto;

import java.util.SortedMap;

public interface IAPIStatesDTO {

	SortedMap<String, IAPIStateDTO> getStatesMapedByURL();

	IAPIStateDTO getTilesState();

	IAPIStateDTO getObjectiveNamesState();

	IAPIStateDTO getMatchesState();

	IAPIStateDTO getMatchDetailsState();

	IAPIStateDTO getWorldNamesState();

	IAPIStateDTO getRecipesState();

	IAPIStateDTO getRecipeDetailsState();

	IAPIStateDTO getMapsState();

	IAPIStateDTO getMapNamesState();

	IAPIStateDTO getMapFloorState();

	IAPIStateDTO getItemsState();

	IAPIStateDTO getItemDetailsState();

	IAPIStateDTO getGuildDetailsState();

	IAPIStateDTO getEventsState();

	IAPIStateDTO getEventNamesState();

	IAPIStateDTO getEventsDetailsState();

	IAPIStateDTO getContinentsState();

	IAPIStateDTO getColorsState();

	IAPIStateDTO getBuildState();

	IAPIStateDTO getStatesState();

	IAPIStateDTO getStateCodesState();

}
