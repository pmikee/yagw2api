package de.justi.yagw2api.core.arenanet.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import de.justi.yagw2api.core.arenanet.dto.IGuildDetailsDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWDTOFactory;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDetailsDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchesDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWObjectiveNameDTO;
import de.justi.yagw2api.core.arenanet.dto.IWorldNameDTO;

class WVWDTOFactory implements IWVWDTOFactory {
	private static final Logger LOGGER = Logger.getLogger(WVWDTOFactory.class);

	public WVWDTOFactory() {

	}

	private Gson createGSON() {
		return new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setVersion(1.0).create();
	}

	public IWVWMatchesDTO newMatchesOf(String json) {
		LOGGER.trace("Going to built " + IWVWMatchesDTO.class.getSimpleName());
		WVWMatchesDTO result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), WVWMatchesDTO.class);
		}catch(JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: "+json, e);
		}
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}

	public IWVWMatchDetailsDTO newMatchDetailsOf(String json) {
		LOGGER.trace("Going to built " + IWVWMatchDetailsDTO.class.getSimpleName());
		WVWMatchDetailsDTO result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), WVWMatchDetailsDTO.class);
		}catch(JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: "+json, e);
		}
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}

	public IWVWObjectiveNameDTO[] newObjectiveNamesOf(String json) {
		LOGGER.trace("Going to built all " + IWVWObjectiveNameDTO.class.getSimpleName());
		WVWObjectiveNameDTO[] result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), WVWObjectiveNameDTO[].class);
		}catch(JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: "+json, e);
		} 
		checkState(result != null);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Built " + Arrays.deepToString(result));
		}
		return result;
	}

	public IWorldNameDTO[] newWorldNamesOf(String json) {
		LOGGER.trace("Going to built " + IWorldNameDTO.class.getSimpleName());
		WorldNameDTO[] result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), WorldNameDTO[].class);
		}catch(JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: "+json, e);
		}
		checkState(result != null);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Built " + Arrays.deepToString(result));
		}
		return result;
	}

	@Override
	public IGuildDetailsDTO newGuildDetailsOf(String json) {
		LOGGER.trace("Going to built " + IGuildDetailsDTO.class.getSimpleName());
		GuildDetailsDTO result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), GuildDetailsDTO.class);
		}catch(JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: "+json, e);
		}
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}
}
