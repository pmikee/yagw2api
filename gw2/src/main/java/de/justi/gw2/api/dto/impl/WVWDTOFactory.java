package de.justi.gw2.api.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;

import org.apache.log4j.Logger;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.justi.gw2.api.dto.IWVWDTOFactory;
import de.justi.gw2.api.dto.IWVWMatchDetailsDTO;
import de.justi.gw2.api.dto.IWVWMatchesDTO;
import de.justi.gw2.api.dto.IWVWObjectiveNameDTO;
import de.justi.gw2.api.dto.IWorldNameDTO;
import de.justi.gw2.api.service.IWVWService;

class WVWDTOFactory implements IWVWDTOFactory {
	private static final Logger LOGGER = Logger.getLogger(WVWDTOFactory.class);

	public WVWDTOFactory() {

	}

	private Gson createGSON(final IWVWService service) {
		return new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setVersion(1.0).create();
	}

	public IWVWMatchesDTO newMatchesOf(String json, IWVWService service) {
		checkNotNull(service);
		LOGGER.trace("Going to built " + IWVWMatchesDTO.class.getSimpleName() + " using service=" + service);
		final WVWMatchesDTO result = this.createGSON(service).fromJson(checkNotNull(json), WVWMatchesDTO.class);
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}

	public IWVWMatchDetailsDTO newMatchDetailsOf(String json, IWVWService service) {
		checkNotNull(service);
		LOGGER.trace("Going to built " + IWVWMatchDetailsDTO.class.getSimpleName() + " using service=" + service);
		final WVWMatchDetailsDTO result = this.createGSON(service).fromJson(checkNotNull(json), WVWMatchDetailsDTO.class);
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}

	public IWVWObjectiveNameDTO[] newObjectiveNamesOf(String json, IWVWService service) {
		checkNotNull(service);
		LOGGER.trace("Going to built all " + IWVWObjectiveNameDTO.class.getSimpleName() + " using service=" + service);
		final WVWObjectiveNameDTO[] result = this.createGSON(service).fromJson(checkNotNull(json), WVWObjectiveNameDTO[].class);
		checkState(result != null);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Built " + Arrays.deepToString(result));
		}
		return result;
	}

	public IWorldNameDTO[] newWorldNamesOf(String json, IWVWService service) {
		checkNotNull(service);
		LOGGER.trace("Going to built " + IWorldNameDTO.class.getSimpleName() + " using service=" + service);
		final WorldNameDTO[] result = this.createGSON(service).fromJson(checkNotNull(json), WorldNameDTO[].class);
		checkState(result != null);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Built " + Arrays.deepToString(result));
		}
		return result;
	}
}
