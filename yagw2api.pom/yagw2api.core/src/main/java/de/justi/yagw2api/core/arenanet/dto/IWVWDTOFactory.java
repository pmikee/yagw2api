package de.justi.yagw2api.core.arenanet.dto;

import de.justi.yagw2api.core.arenanet.service.IWVWService;


public interface IWVWDTOFactory {
	IWVWMatchesDTO newMatchesOf(String json, IWVWService service);
	IWVWMatchDetailsDTO newMatchDetailsOf(String json, IWVWService service);
	IWVWObjectiveNameDTO[] newObjectiveNamesOf(String json, IWVWService service);
	IWorldNameDTO[] newWorldNamesOf(String json, IWVWService service);
}
