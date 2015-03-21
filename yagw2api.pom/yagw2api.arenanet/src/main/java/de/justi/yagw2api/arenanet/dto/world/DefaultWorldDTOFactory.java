package de.justi.yagw2api.arenanet.dto.world;

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
import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;

import de.justi.yagw2api.arenanet.dto.AbstractGSONFactory;

public final class DefaultWorldDTOFactory extends AbstractGSONFactory implements WorldDTOFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWorldDTOFactory.class);

	public DefaultWorldDTOFactory() {
		super();
	}

	@Override
	public WorldNameDTO[] newWorldNamesOf(final String json) {
		DefaultWorldNameDTO[] result;
		try {
			result = this.getGSON().fromJson(checkNotNull(json), DefaultWorldNameDTO[].class);
		} catch (JsonSyntaxException e) {
			result = null;
			LOGGER.error("received invalid json: {}", json, e);
		}
		checkState(result != null);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Built {}", Arrays.deepToString(result));
		}
		return result;
	}
}
