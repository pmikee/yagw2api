package api.service;

import java.util.Locale;

import api.service.dto.IWVWMatchDTO;
import api.service.dto.IWVWMatchDetailsDTO;
import api.service.dto.IWVWMatchesDTO;
import api.service.dto.IWVWObjectiveNameDTO;
import api.service.dto.IWorldNameDTO;

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
