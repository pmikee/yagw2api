package api.service;

import java.util.Locale;

import api.service.dto.IWVWMatchDetailsDTO;
import api.service.dto.IWVWMatchesDTO;
import api.service.dto.IWVWObjectiveNameDTO;
import api.service.dto.IWorldNameDTO;

public interface IWVWService {
	IWVWMatchesDTO retrieveAllMatches();
	IWVWMatchDetailsDTO retrieveMatchDetails(String id);
	IWVWObjectiveNameDTO[] retrieveAllObjectiveNames(Locale locale);
	IWorldNameDTO[] retrieveAllWorldNames(Locale locale);
}
