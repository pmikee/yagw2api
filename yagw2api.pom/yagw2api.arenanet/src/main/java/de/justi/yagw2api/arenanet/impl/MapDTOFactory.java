package de.justi.yagw2api.arenanet.impl;

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
