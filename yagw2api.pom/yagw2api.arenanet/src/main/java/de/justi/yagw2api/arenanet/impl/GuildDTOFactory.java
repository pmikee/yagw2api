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

import de.justi.yagw2api.arenanet.IGuildDTOFactory;
import de.justi.yagw2api.arenanet.IGuildDetailsDTO;

final class GuildDTOFactory extends AbstractGSONFactory implements IGuildDTOFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(GuildDTOFactory.class);

	public GuildDTOFactory() {
		super();
	}

	@Override
	public IGuildDetailsDTO newGuildDetailsOf(final String json) {
		LOGGER.trace("Going to built " + IGuildDetailsDTO.class.getSimpleName());
		GuildDetailsDTO result;
		try {
			result = this.getGSON().fromJson(checkNotNull(json), GuildDetailsDTO.class);
		} catch (JsonSyntaxException e) {
			LOGGER.error("Invalid json received: {}", json);
			throw e;
		}
		LOGGER.debug("Built {}", result);
		return result;
	}
}
