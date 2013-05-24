package api.service.dto;


public interface IWVWDTOFactory {
	IWVWMatchesDTO createMatchesDTOfromJSON(String json);
	IWVWMatchDetailsDTO createMatchDetailsfromJSON(String json);
	IWVWObjectiveNameDTO[] createObjectiveNamesFromJSON(String json);
}
