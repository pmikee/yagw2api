package de.justi.yagw2api.arenanet;

import java.util.Locale;

import com.google.common.base.Optional;

public interface IMapFloorService {
	Optional<IMapFloorDTO> retrieveMapFloor(final int continentId, final int floor, final Locale lang);
}
