package de.justi.yagw2api.arenanet;

import java.util.Locale;

import com.google.common.base.Optional;

public interface IWorldService {

	Optional<IWorldNameDTO> retrieveWorldName(Locale locale, int worldId);

	IWorldNameDTO[] retrieveAllWorldNames(Locale locale);

}
