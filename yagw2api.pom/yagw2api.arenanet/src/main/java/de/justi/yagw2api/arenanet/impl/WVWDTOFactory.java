package de.justi.yagw2api.arenanet.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import de.justi.yagw2api.arenanet.IWVWDTOFactory;
import de.justi.yagw2api.arenanet.IWVWMatchDetailsDTO;
import de.justi.yagw2api.arenanet.IWVWMatchesDTO;
import de.justi.yagw2api.arenanet.IWVWObjectiveNameDTO;

final class WVWDTOFactory implements IWVWDTOFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(WVWDTOFactory.class);

	public WVWDTOFactory() {

	}

	private Gson createGSON() {
		return new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setVersion(1.0).create();
	}

	@Override
	public IWVWMatchesDTO newMatchesOf(String json) {
		LOGGER.trace("Going to built " + IWVWMatchesDTO.class.getSimpleName());
		final WVWMatchesDTO result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), WVWMatchesDTO.class);
		} catch (JsonSyntaxException e) {
			LOGGER.error("Invalid response: {}", json, e);
			throw e;
		}
		LOGGER.debug("Built {}", result);
		return result;
	}

	@Override
	public IWVWMatchDetailsDTO newMatchDetailsOf(String json) {
		LOGGER.trace("Going to built " + IWVWMatchDetailsDTO.class.getSimpleName());
		final WVWMatchDetailsDTO result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), WVWMatchDetailsDTO.class);
		} catch (JsonSyntaxException e) {
			LOGGER.error("Invalid response: {}", json, e);
			throw e;
		}
		LOGGER.debug("Built {}", result);
		return result;
	}

	@Override
	public IWVWObjectiveNameDTO[] newObjectiveNamesOf(String json) {
		LOGGER.trace("Going to built all " + IWVWObjectiveNameDTO.class.getSimpleName());
		final WVWObjectiveNameDTO[] result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), WVWObjectiveNameDTO[].class);
		} catch (JsonSyntaxException e) {
			LOGGER.error("Invalid response: {}", json, e);
			throw e;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Built {}", Arrays.deepToString(result));
		}
		return result;
	}

}
