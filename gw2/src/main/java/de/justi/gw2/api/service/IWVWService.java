package de.justi.gw2.api.service;

import java.util.Locale;


import com.google.common.base.Optional;

import de.justi.gw2.api.dto.IWVWMatchDTO;
import de.justi.gw2.api.dto.IWVWMatchDetailsDTO;
import de.justi.gw2.api.dto.IWVWMatchesDTO;
import de.justi.gw2.api.dto.IWVWObjectiveNameDTO;
import de.justi.gw2.api.dto.IWorldNameDTO;

public interface IWVWService {
	IWVWMatchesDTO retrieveAllMatches();
	IWVWMatchDetailsDTO retrieveMatchDetails(String id);
	IWVWObjectiveNameDTO[] retrieveAllObjectiveNames(Locale locale);
	IWorldNameDTO[] retrieveAllWorldNames(Locale locale);
	
	Optional<IWVWMatchDTO> retrieveMatch(String matchId);
	Optional<IWVWObjectiveNameDTO> retrieveObjectiveName(Locale locale, int id);
	Optional<IWorldNameDTO> retrieveWorldName(Locale locale, int worldId);
}
