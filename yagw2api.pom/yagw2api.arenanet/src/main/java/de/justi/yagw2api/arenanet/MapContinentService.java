package de.justi.yagw2api.arenanet;

import java.util.Locale;

import de.justi.yagw2api.arenanet.dto.map.MapContinentWithIdDTO;

public interface MapContinentService {

	Iterable<MapContinentWithIdDTO> retrieveAllContinents();

	Iterable<MapContinentWithIdDTO> retrieveAllContinents(Locale lang);
}
