package de.justi.yagw2api.arenanet.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import de.justi.yagw2api.arenanet.IGuildDTOFactory;
import de.justi.yagw2api.arenanet.IGuildDetailsDTO;
import de.justi.yagw2api.arenanet.IGuildService;
import de.justi.yagwapi.common.RetryClientFilter;

final class GuildService implements IGuildService {
	private static final Logger LOGGER = LoggerFactory.getLogger(GuildService.class);
	private static final long GUILD_DETAILS_CACHE_EXPIRE_MILLIS = 1000 * 60 * 5; // 5m

	private static final URL GUILD_DETAILS_URL;
	static {
		try {
			GUILD_DETAILS_URL = new URL("https://api.guildwars2.com/" + ServiceUtils.API_VERSION + "/guild_details.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}
	}

	// FIELDS
	private final Cache<String, Optional<IGuildDetailsDTO>> guildDetailsCache = CacheBuilder.newBuilder().expireAfterWrite(GUILD_DETAILS_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build();
	private final IGuildDTOFactory guildDTOFactory;

	// METHODS
	@Inject
	public GuildService(IGuildDTOFactory guildDTOFactory) {
		this.guildDTOFactory = checkNotNull(guildDTOFactory);
	}

	@Override
	public Optional<IGuildDetailsDTO> retrieveGuildDetails(final String id) {
		checkNotNull(id);
		try {
			return this.guildDetailsCache.get(id, new Callable<Optional<IGuildDetailsDTO>>() {
				@Override
				public Optional<IGuildDetailsDTO> call() throws Exception {
					final WebResource resource = ServiceUtils.REST_CLIENT.resource(GUILD_DETAILS_URL.toExternalForm()).queryParam("guild_id", id);
					resource.addFilter(new RetryClientFilter(ServiceUtils.REST_RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response=" + response);
						final IGuildDetailsDTO result = GuildService.this.guildDTOFactory.newGuildDetailsOf(response);
						LOGGER.debug("Built result=" + result);
						return Optional.of(result);
					} catch (ClientHandlerException | UniformInterfaceException e) {
						LOGGER.error("Exception thrown while quering {}",resource, e);
						return Optional.absent();
					}
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve " + IGuildDetailsDTO.class.getSimpleName() + " from cache for id=" + id, e);
			throw new IllegalStateException("Failed to retrieve " + IGuildDetailsDTO.class.getSimpleName() + " from cache for id=" + id, e);
		}
	}
}
