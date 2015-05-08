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
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import de.justi.yagw2api.arenanet.dto.map.MapDTOFactory;
import de.justi.yagw2api.arenanet.dto.map.MapFloorDTO;
import de.justi.yagwapi.common.RetryClientFilter;
import de.justi.yagwapi.common.Tuple3;
import de.justi.yagwapi.common.Tuples;

final class DefaultMapFloorService implements MapFloorService {
	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMapFloorService.class);
	private static final long CACHE_EXPIRE_MILLIS = TimeUnit.HOURS.toMillis(12);
	private static final URL MAP_FLOOR_URL;
	static {
		try {
			MAP_FLOOR_URL = new URL("https://api.guildwars2.com/" + ArenanetUtils.API_VERSION + "/map_floor.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}
	}

	// FIELDS
	private final MapDTOFactory mapDTOFactory;
	private final Locale defaultLocale;
	private final LoadingCache<Tuple3<Locale, String, Integer>, Optional<MapFloorDTO>> mapFloorCache = CacheBuilder.newBuilder()
			.expireAfterWrite(CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build(new CacheLoader<Tuple3<Locale, String, Integer>, Optional<MapFloorDTO>>() {
				@Override
				public Optional<MapFloorDTO> load(final Tuple3<Locale, String, Integer> key) throws Exception {
					final WebResource resource = ArenanetUtils.REST_CLIENT.resource(MAP_FLOOR_URL.toExternalForm()).queryParam("continent_id ", key.getValue2().get().toString())
							.queryParam("floor", key.getValue3().get().toString()).queryParam("lang", key.getValue1().get().toLanguageTag());
					try {
						resource.addFilter(new RetryClientFilter(ArenanetUtils.REST_RETRY_COUNT));
						final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response=" + response);
						final MapFloorDTO result = DefaultMapFloorService.this.mapDTOFactory.newMapFloorOf(response);
						LOGGER.trace("Built result=" + result);
						return Optional.of(result);
					} catch (ClientHandlerException | UniformInterfaceException e) {
						LOGGER.error("Exception thrown while quering {}", resource, e);
						return Optional.absent();
					}
				}
			});

	// CONSTRUCTOR
	@Inject
	public DefaultMapFloorService(final Locale defaultLocale, final MapDTOFactory mapDTOFactory) {
		this.mapDTOFactory = checkNotNull(mapDTOFactory, "missing mapDTOFactory");
		this.defaultLocale = checkNotNull(defaultLocale, "missing defaultLocale");
	}

	// METHODS
	@Override
	public Optional<MapFloorDTO> retrieveMapFloor(final String continentId, final int floor, final Locale lang) {
		checkNotNull(lang, "missing lang");
		try {
			return this.mapFloorCache.get(Tuples.of(lang, continentId, floor));
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve {} from cache for continentId={}, floor={}, lang={}", MapFloorDTO.class, continentId, floor, lang, e);
			throw new IllegalStateException("Failed to retrieve " + MapFloorDTO.class.getSimpleName() + " from cache for continentId=" + continentId + ", floor=" + floor
					+ ", lang=" + lang, e);
		}
	}

	@Override
	public Optional<MapFloorDTO> retrieveMapFloor(final String continentId, final int floor) {
		return this.retrieveMapFloor(continentId, floor, this.defaultLocale);
	}
}
