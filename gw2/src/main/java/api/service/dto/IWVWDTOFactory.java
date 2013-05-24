package api.service.dto;

import api.service.IWVWService;


public interface IWVWDTOFactory {
	IWVWMatchesDTO createMatchesDTOfromJSON(String json, IWVWService service);
	IWVWMatchDetailsDTO createMatchDetailsfromJSON(String json, IWVWService service);
	IWVWObjectiveNameDTO[] createObjectiveNamesFromJSON(String json, IWVWService service);
	IWorldNameDTO[] createWorldNamesFromJSON(String json, IWVWService service);
}
