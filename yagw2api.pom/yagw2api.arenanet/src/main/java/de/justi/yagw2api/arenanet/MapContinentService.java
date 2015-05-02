package de.justi.yagw2api.arenanet;

import java.util.Locale;

import de.justi.yagw2api.arenanet.dto.map.MapContinentDTO;

public interface MapContinentService {

	Iterable<MapContinentDTO> retrieveAllContinents(Locale lang);
}
