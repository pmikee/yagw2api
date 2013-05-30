package de.justi.yagw2api.core.api.dto;

import de.justi.yagw2api.core.api.service.IWVWService;


public interface IWVWDTOFactory {
	IWVWMatchesDTO newMatchesOf(String json, IWVWService service);
	IWVWMatchDetailsDTO newMatchDetailsOf(String json, IWVWService service);
	IWVWObjectiveNameDTO[] newObjectiveNamesOf(String json, IWVWService service);
	IWorldNameDTO[] newWorldNamesOf(String json, IWVWService service);
}
