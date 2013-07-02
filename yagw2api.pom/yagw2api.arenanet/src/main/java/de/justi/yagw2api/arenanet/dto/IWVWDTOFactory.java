package de.justi.yagw2api.arenanet.dto;



public interface IWVWDTOFactory {
	IWVWMatchesDTO newMatchesOf(String json);
	IWVWMatchDetailsDTO newMatchDetailsOf(String json);
	IGuildDetailsDTO newGuildDetailsOf(String json);
	IWVWObjectiveNameDTO[] newObjectiveNamesOf(String json);
	IWorldNameDTO[] newWorldNamesOf(String json);
}
