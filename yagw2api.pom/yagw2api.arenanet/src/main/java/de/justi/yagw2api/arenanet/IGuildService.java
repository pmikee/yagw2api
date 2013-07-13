package de.justi.yagw2api.arenanet;

import com.google.common.base.Optional;

public interface IGuildService {
	Optional<IGuildDetailsDTO> retrieveGuildDetails(String id);

}
