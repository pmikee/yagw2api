package de.justi.yagw2api.arenanet.v1.dto.map;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import de.justi.yagw2api.arenanet.v1.dto.AbstractGSONFactory;

public final class DefaultMapDTOFactory extends AbstractGSONFactory implements MapDTOFactory {
	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMapDTOFactory.class);
	private static final JsonParser JSON_PARSER = new JsonParser();

	// CONSTRUCTOR
	public DefaultMapDTOFactory() {
		super();
	}

	@Override
	public DefaultMapFloorDTO newMapFloorOf(final String json) {
		try {
			final DefaultMapFloorDTO result = this.getGSON().fromJson(checkNotNull(json), DefaultMapFloorDTO.class);
			LOGGER.trace("Built {}", result);
			return result;
		} catch (JsonSyntaxException e) {
			return this.tryToHandleEmptyStringForMissingRegionsInMapFloorDTO(e, json);
		}
	}

	/**
	 * check if exception was caused by bad api design that causes missing regions to be serialized as empty string instead of an empty object
	 *
	 * @param originalException
	 * @param json
	 * @return
	 * @throws JsonSyntaxException
	 */
	private DefaultMapFloorDTO tryToHandleEmptyStringForMissingRegionsInMapFloorDTO(final JsonSyntaxException originalException, final String json) throws JsonSyntaxException {
		try {
			final JsonObject root = JSON_PARSER.parse(json).getAsJsonObject();
			final String regions = root.get("regions").getAsString();
			if (regions.isEmpty()) {
				final JsonArray textureDims = root.get("texture_dims").getAsJsonArray();
				final JsonArray clampedView = root.get("clamped_view").getAsJsonArray();
				//@formatter:off
				return new DefaultMapFloorDTO(
							new int[] {
								textureDims.get(0).getAsInt(),
								textureDims.get(1).getAsInt()
							},
							new int[][]{
								{
									clampedView.get(0).getAsJsonArray().get(0).getAsInt(),
									clampedView.get(0).getAsJsonArray().get(1).getAsInt()
								},
								{
									clampedView.get(1).getAsJsonArray().get(0).getAsInt(),
									clampedView.get(1).getAsJsonArray().get(1).getAsInt()
								}
							});
				//@formatter:on
			} else {
				LOGGER.debug("Unable to fix {}, because it was not caused by empty region string", originalException.getClass());
				throw originalException;
			}
		} catch (JsonSyntaxException e) {
			LOGGER.error("Invalid response: {}", json, e);
			throw e;
		}
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
