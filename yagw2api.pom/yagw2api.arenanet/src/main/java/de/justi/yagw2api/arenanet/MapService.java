package de.justi.yagw2api.arenanet;

import java.util.Locale;

import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.dto.map.MapsDTO;

public interface MapService {

	Optional<MapsDTO> retrieveAllMaps(Locale locale);
}
