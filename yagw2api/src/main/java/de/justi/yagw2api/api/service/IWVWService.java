package de.justi.yagw2api.api.service;

import java.util.Locale;


import com.google.common.base.Optional;

import de.justi.yagw2api.api.dto.IWVWMatchDTO;
import de.justi.yagw2api.api.dto.IWVWMatchDetailsDTO;
import de.justi.yagw2api.api.dto.IWVWMatchesDTO;
import de.justi.yagw2api.api.dto.IWVWObjectiveNameDTO;
import de.justi.yagw2api.api.dto.IWorldNameDTO;

public interface IWVWService {
	IWVWMatchesDTO retrieveAllMatches();
	IWVWMatchDetailsDTO retrieveMatchDetails(String id);
	IWVWObjectiveNameDTO[] retrieveAllObjectiveNames(Locale locale);
	IWorldNameDTO[] retrieveAllWorldNames(Locale locale);
	
	Optional<IWVWMatchDTO> retrieveMatch(String matchId);
	Optional<IWVWObjectiveNameDTO> retrieveObjectiveName(Locale locale, int id);
	Optional<IWorldNameDTO> retrieveWorldName(Locale locale, int worldId);
}
