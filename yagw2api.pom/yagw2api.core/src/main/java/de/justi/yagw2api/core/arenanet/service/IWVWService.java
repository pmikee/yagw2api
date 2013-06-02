package de.justi.yagw2api.core.arenanet.service;

import java.text.DateFormat;
import java.util.Locale;


import com.google.common.base.Optional;

import de.justi.yagw2api.core.arenanet.dto.IGuildDetailsDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDetailsDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchesDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWObjectiveNameDTO;
import de.justi.yagw2api.core.arenanet.dto.IWorldNameDTO;

public interface IWVWService {
	DateFormat getZuluDateFormat();
	
	IWVWMatchesDTO retrieveAllMatches();
	IWVWObjectiveNameDTO[] retrieveAllObjectiveNames(Locale locale);
	IWorldNameDTO[] retrieveAllWorldNames(Locale locale);
	
	Optional<IGuildDetailsDTO> retrieveGuildDetails(String id);
	Optional<IWVWMatchDetailsDTO> retrieveMatchDetails(String id);	
	Optional<IWVWMatchDTO> retrieveMatch(String matchId);
	Optional<IWVWObjectiveNameDTO> retrieveObjectiveName(Locale locale, int id);
	Optional<IWorldNameDTO> retrieveWorldName(Locale locale, int worldId);
}
