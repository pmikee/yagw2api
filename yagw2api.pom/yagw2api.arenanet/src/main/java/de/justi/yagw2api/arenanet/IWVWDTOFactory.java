package de.justi.yagw2api.arenanet;

public interface IWVWDTOFactory {
	IWVWMatchesDTO newMatchesOf(String json);

	IWVWMatchDetailsDTO newMatchDetailsOf(String json);

	IWVWObjectiveNameDTO[] newObjectiveNamesOf(String json);
}
