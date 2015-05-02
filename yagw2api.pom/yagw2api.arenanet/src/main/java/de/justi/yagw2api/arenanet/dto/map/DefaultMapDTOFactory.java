package de.justi.yagw2api.arenanet.dto.map;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */
import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;

import de.justi.yagw2api.arenanet.dto.AbstractGSONFactory;

public final class DefaultMapDTOFactory extends AbstractGSONFactory implements MapDTOFactory {
	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMapDTOFactory.class);

	// CONSTRUCTOR
	public DefaultMapDTOFactory() {
		super();
	}

	@Override
	public DefaultMapFloorDTO newMapFloorOf(final String json) {
		final DefaultMapFloorDTO result;
		try {
			result = this.getGSON().fromJson(checkNotNull(json), DefaultMapFloorDTO.class);
		} catch (JsonSyntaxException e) {
			LOGGER.error("Invalid response: {}", json, e);
			throw e;
		}
		LOGGER.trace("Built {}", result);
		return result;
	}

	@Override
	public DefaultMapsDTO newMapsOf(final String json) {
		final DefaultMapsDTO result;
		try {
			result = this.getGSON().fromJson(checkNotNull(json), DefaultMapsDTO.class);
		} catch (JsonSyntaxException e) {
			LOGGER.error("Invalid response: {}", json, e);
			throw e;
		}
		LOGGER.trace("Built {}", result);
		return result;
	}

	@Override
	public DefaultMapContinentsDTO newMapContinentsOf(final String json) {
		final DefaultMapContinentsDTO result;
		try {
			result = this.getGSON().fromJson(checkNotNull(json), DefaultMapContinentsDTO.class);
		} catch (JsonSyntaxException e) {
			LOGGER.error("Invalid response: {}", json, e);
			throw e;
		}
		LOGGER.trace("Built {}", result);
		return result;
	}

}
