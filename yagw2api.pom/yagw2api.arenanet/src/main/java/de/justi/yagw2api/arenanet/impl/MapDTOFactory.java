package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;

import de.justi.yagw2api.arenanet.IMapDTOFactory;
import de.justi.yagw2api.arenanet.IMapFloorDTO;

final class MapDTOFactory extends AbstractGSONFactory implements IMapDTOFactory {
	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(MapDTOFactory.class);

	// CONSTRUCTOR
	public MapDTOFactory() {
		super();
	}

	@Override
	public IMapFloorDTO newMapFloorOf(final String json) {
		final MapFloorDTO result;
		try {
			result = this.getGSON().fromJson(checkNotNull(json), MapFloorDTO.class);
		} catch (JsonSyntaxException e) {
			LOGGER.error("Invalid response: {}", json, e);
			throw e;
		}
		LOGGER.debug("Built {}", result);
		return result;
	}

}
