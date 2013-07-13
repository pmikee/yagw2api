package de.justi.yagw2api.arenanet;

import java.util.Locale;

import com.google.common.base.Optional;

public interface IWVWService {

	IWVWMatchesDTO retrieveAllMatches();

	IWVWObjectiveNameDTO[] retrieveAllObjectiveNames(Locale locale);

	Optional<IWVWMatchDetailsDTO> retrieveMatchDetails(String id);

	Optional<IWVWMatchDTO> retrieveMatch(String matchId);

	Optional<IWVWObjectiveNameDTO> retrieveObjectiveName(Locale locale, int id);
}
