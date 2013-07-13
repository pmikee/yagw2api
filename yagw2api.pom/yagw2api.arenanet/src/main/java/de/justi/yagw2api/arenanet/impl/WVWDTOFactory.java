package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import de.justi.yagw2api.arenanet.IWVWDTOFactory;
import de.justi.yagw2api.arenanet.IWVWMatchDetailsDTO;
import de.justi.yagw2api.arenanet.IWVWMatchesDTO;
import de.justi.yagw2api.arenanet.IWVWObjectiveNameDTO;

final class WVWDTOFactory implements IWVWDTOFactory {
	private static final Logger LOGGER = Logger.getLogger(WVWDTOFactory.class);

	public WVWDTOFactory() {

	}

	private Gson createGSON() {
		return new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setVersion(1.0).create();
	}

	@Override
	public IWVWMatchesDTO newMatchesOf(String json) {
		LOGGER.trace("Going to built " + IWVWMatchesDTO.class.getSimpleName());
		WVWMatchesDTO result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), WVWMatchesDTO.class);
		} catch (JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: " + json, e);
		}
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}

	@Override
	public IWVWMatchDetailsDTO newMatchDetailsOf(String json) {
		LOGGER.trace("Going to built " + IWVWMatchDetailsDTO.class.getSimpleName());
		WVWMatchDetailsDTO result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), WVWMatchDetailsDTO.class);
		} catch (JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: " + json, e);
		}
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}

	@Override
	public IWVWObjectiveNameDTO[] newObjectiveNamesOf(String json) {
		LOGGER.trace("Going to built all " + IWVWObjectiveNameDTO.class.getSimpleName());
		WVWObjectiveNameDTO[] result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), WVWObjectiveNameDTO[].class);
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
