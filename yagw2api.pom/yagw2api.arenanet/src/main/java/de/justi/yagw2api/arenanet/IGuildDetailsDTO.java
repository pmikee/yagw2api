package de.justi.yagw2api.arenanet;

import com.google.common.base.Optional;

public interface IGuildDetailsDTO {
	String getId();

	String getName();

	String getTag();

	Optional<IGuildEmblemDTO> getEmblem();
}
