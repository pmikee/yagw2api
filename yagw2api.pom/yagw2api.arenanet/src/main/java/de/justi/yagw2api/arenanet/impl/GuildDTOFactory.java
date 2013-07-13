package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import de.justi.yagw2api.arenanet.IGuildDTOFactory;
import de.justi.yagw2api.arenanet.IGuildDetailsDTO;

final class GuildDTOFactory implements IGuildDTOFactory {
	private static final Logger LOGGER = Logger.getLogger(GuildDTOFactory.class);

	public GuildDTOFactory() {

	}

	private Gson createGSON() {
		return new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setVersion(1.0).create();
	}

	@Override
	public IGuildDetailsDTO newGuildDetailsOf(String json) {
		LOGGER.trace("Going to built " + IGuildDetailsDTO.class.getSimpleName());
		GuildDetailsDTO result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), GuildDetailsDTO.class);
		} catch (JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: " + json, e);
		}
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}
}
