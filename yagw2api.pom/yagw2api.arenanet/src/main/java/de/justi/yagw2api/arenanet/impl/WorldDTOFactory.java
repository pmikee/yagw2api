package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import de.justi.yagw2api.arenanet.IWorldDTOFactory;
import de.justi.yagw2api.arenanet.IWorldNameDTO;

final class WorldDTOFactory implements IWorldDTOFactory {
	private static final Logger LOGGER = Logger.getLogger(WorldDTOFactory.class);

	public WorldDTOFactory() {

	}

	private Gson createGSON() {
		return new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setVersion(1.0).create();
	}

	@Override
	public IWorldNameDTO[] newWorldNamesOf(String json) {
		LOGGER.trace("Going to built " + IWorldNameDTO.class.getSimpleName());
		WorldNameDTO[] result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), WorldNameDTO[].class);
		} catch (JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: " + json, e);
		}
		checkState(result != null);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Built " + Arrays.deepToString(result));
		}
		return result;
	}
}
