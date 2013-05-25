package api.service;

import java.util.Locale;

import api.dto.IWVWMatchDTO;
import api.dto.IWVWMatchDetailsDTO;
import api.dto.IWVWMatchesDTO;
import api.dto.IWVWObjectiveNameDTO;
import api.dto.IWorldNameDTO;

import com.google.common.base.Optional;

public interface IWVWService {
	IWVWMatchesDTO retrieveAllMatches();
	IWVWMatchDetailsDTO retrieveMatchDetails(String id);
	IWVWObjectiveNameDTO[] retrieveAllObjectiveNames(Locale locale);
	IWorldNameDTO[] retrieveAllWorldNames(Locale locale);
	
	Optional<IWVWMatchDTO> retrieveMatch(String matchId);
	Optional<IWVWObjectiveNameDTO> retrieveObjectiveName(Locale locale, int id);
	Optional<IWorldNameDTO> retrieveWorldName(Locale locale, int worldId);
}
