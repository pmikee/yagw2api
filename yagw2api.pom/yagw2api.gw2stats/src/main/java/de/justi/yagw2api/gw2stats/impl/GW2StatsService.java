package de.justi.yagw2api.gw2stats.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-GW2Stats
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
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;

import de.justi.yagw2api.gw2stats.IAPIStateDTO;
import de.justi.yagw2api.gw2stats.IAPIStateDescriptionDTO;
import de.justi.yagw2api.gw2stats.IGW2StatsDTOFactory;
import de.justi.yagw2api.gw2stats.IGW2StatsService;
import de.justi.yagwapi.common.RetryClientFilter;

final class GW2StatsService implements IGW2StatsService {
	private static final int RETRY_COUNT = 10;
	private static final Logger LOGGER = Logger.getLogger(GW2StatsService.class);

	private static final long API_STATES_CACHE_EXPIRE_MILLIS = 1000 * 15; // 15s
	private static final long API_STATE_DESCRIPTIONS_CACHE_EXPIRE_MILLIS = 1000 * 60 * 60; // 1h
	private static final URL API_STATES_URL;
	private static final URL API_STATE_DESCRIPTIONS_URL;
	static {
		try {
			API_STATES_URL = new URL("http://gw2stats.net/api/status.json");
			API_STATE_DESCRIPTIONS_URL = new URL("http://gw2stats.net/api/status_codes.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}
	}

	// injections
	private IGW2StatsDTOFactory gw2statsDTOFactory;

	@Inject
	public GW2StatsService(IGW2StatsDTOFactory gw2statsDTOFactory) {
		checkNotNull(gw2statsDTOFactory);
		this.gw2statsDTOFactory = gw2statsDTOFactory;
	}

	private final Cache<String, Map<String, IAPIStateDTO>> apieStatesCache = CacheBuilder.newBuilder().expireAfterWrite(API_STATES_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build();
	private final Cache<String, Map<String, IAPIStateDescriptionDTO>> apieStateDescriptionsCache = CacheBuilder.newBuilder()
			.expireAfterWrite(API_STATE_DESCRIPTIONS_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build();

	@Override
	public Map<String, IAPIStateDTO> retrieveAPIStates() {
		try {
			return this.apieStatesCache.get("", new Callable<Map<String, IAPIStateDTO>>() {
				@Override
				public Map<String, IAPIStateDTO> call() throws Exception {
					try {
						final WebResource resource = ServiceConstants.REST_CLIENT.resource(API_STATES_URL.toExternalForm());
						resource.addFilter(new RetryClientFilter(RETRY_COUNT));
						final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
						try {
							final String response = builder.get(String.class).replaceAll(Pattern.quote("\\"), "");
							LOGGER.trace("Retrieved response=" + response);
							final Map<String, IAPIStateDTO> result = GW2StatsService.this.gw2statsDTOFactory.newAPIStatesOf(response);
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Built result=" + result);
							}
							return result;
						} catch (ClientHandlerException e) {
							LOGGER.fatal("Exception thrown while quering " + resource.getURI(), e);
							return null;
						}
					} catch (Throwable e) {
						LOGGER.fatal("Failed to retrieve cache result.", e);
						throw e;
					}
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve " + Map.class.getSimpleName() + " of " + IAPIStateDTO.class.getSimpleName() + " from cache.", e);
			throw new IllegalStateException("Failed to retrieve " + Map.class.getSimpleName() + " of " + IAPIStateDTO.class.getSimpleName() + " from cache.", e);
		}

	}

	@Override
	public Map<String, IAPIStateDescriptionDTO> retrieveAPIStateDescriptions() {
		try {
			return this.apieStateDescriptionsCache.get("", new Callable<Map<String, IAPIStateDescriptionDTO>>() {
				@Override
				public Map<String, IAPIStateDescriptionDTO> call() throws Exception {
					final WebResource resource = ServiceConstants.REST_CLIENT.resource(API_STATE_DESCRIPTIONS_URL.toExternalForm());
					resource.addFilter(new RetryClientFilter(RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response=" + response);
						final Map<String, IAPIStateDescriptionDTO> result = GW2StatsService.this.gw2statsDTOFactory.newAPIStateDescriptionsOf(response);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Built result=" + result);
						}
						return result;
					} catch (ClientHandlerException e) {
						LOGGER.fatal("Exception thrown while quering " + resource.getURI(), e);
						return null;
					}
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve " + Map.class.getSimpleName() + " of " + IAPIStateDTO.class.getSimpleName() + " from cache.", e);
			throw new IllegalStateException("Failed to retrieve " + Map.class.getSimpleName() + " of " + IAPIStateDTO.class.getSimpleName() + " from cache.", e);
		}
	}

	@Override
	public Optional<IAPIStateDescriptionDTO> retrieveAPIStateDescription(String state) {
		return Optional.fromNullable(this.retrieveAPIStateDescriptions().get(state));
	}
}
