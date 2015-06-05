package de.justi.yagw2api.arenanet;

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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import de.justi.yagw2api.arenanet.dto.map.MapContinentDTO;
import de.justi.yagw2api.arenanet.dto.map.MapContinentWithIdDTO;
import de.justi.yagw2api.arenanet.dto.map.MapContinentsDTO;
import de.justi.yagw2api.arenanet.dto.map.MapDTOFactory;
import de.justi.yagw2api.common.rest.RetryClientFilter;

final class DefaultMapContinentService implements MapContinentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMapContinentService.class);
	private static final long CACHE_EXPIRE_MILLIS = TimeUnit.HOURS.toMillis(12);
	private static final URL MAP_CONTINENTS_URL;
	static {
		try {
			MAP_CONTINENTS_URL = new URL("https://api.guildwars2.com/" + ArenanetUtils.API_VERSION + "/continents.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}
	}

	// FIELDS
	private final LoadingCache<Locale, Iterable<MapContinentWithIdDTO>> mapContinentsCache = CacheBuilder.newBuilder().expireAfterWrite(CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS)
			.build(new CacheLoader<Locale, Iterable<MapContinentWithIdDTO>>() {
				@Override
				public Iterable<MapContinentWithIdDTO> load(final Locale key) throws Exception {
					final WebResource resource = ArenanetUtils.REST_CLIENT.resource(MAP_CONTINENTS_URL.toExternalForm()).queryParam("lang", key.toLanguageTag());
					try {
						resource.addFilter(new RetryClientFilter(ArenanetUtils.REST_RETRY_COUNT));
						final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response=" + response);
						final MapContinentsDTO result = DefaultMapContinentService.this.mapDTOFactory.newMapContinentsOf(response);
						LOGGER.debug("Built result=" + result);
						return ImmutableList.copyOf(result.getContinents());
					} catch (ClientHandlerException | UniformInterfaceException e) {
						LOGGER.error("Exception thrown while quering {}", resource, e);
						return Collections.emptyList();
					}
				}
			});
	private final MapDTOFactory mapDTOFactory;

	// CONSTRUCTOR
	@Inject
	public DefaultMapContinentService(final MapDTOFactory mapDTOFactory) {
		this.mapDTOFactory = checkNotNull(mapDTOFactory);
	}

	// METHODS

	@Override
	public Iterable<MapContinentWithIdDTO> retrieveAllContinents(final Locale lang) {
		checkNotNull(lang, "missing lang");
		try {
			return this.mapContinentsCache.get(lang);
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve {} from cache for  lang={}", MapContinentDTO.class, lang, e);
			throw new IllegalStateException("Failed to retrieve " + MapContinentDTO.class.getSimpleName() + " from cache forlang=" + lang, e);
		}
	}

	@Override
	public Iterable<MapContinentWithIdDTO> retrieveAllContinents() {
		return this.retrieveAllContinents(YAGW2APIArenanet.INSTANCE.getCurrentLocale());
	}
}
