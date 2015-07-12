package de.justi.yagw2api.arenanet.v1;

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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import de.justi.yagw2api.arenanet.v1.dto.world.WorldNameDTO;
import de.justi.yagw2api.arenanet.v1.dto.wvw.WVWDTOFactory;
import de.justi.yagw2api.arenanet.v1.dto.wvw.WVWMatchDTO;
import de.justi.yagw2api.arenanet.v1.dto.wvw.WVWMatchDetailsDTO;
import de.justi.yagw2api.arenanet.v1.dto.wvw.WVWMatchesDTO;
import de.justi.yagw2api.arenanet.v1.dto.wvw.WVWObjectiveNameDTO;
import de.justi.yagw2api.common.rest.RetryClientFilter;

final class DefaultWVWService implements WVWService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWVWService.class);
	private static final long MATCH_CACHE_EXPIRE_MILLIS = 1000 * 60 * 10; // 10m
	private static final long MATCH_DETAILS_CACHE_EXPIRE_MILLIS = 1000 * 3; // 3s
	private static final long OBJECTIVE_NAMES_CACHE_EXPIRE_MILLIS = 1000 * 60 * 60 * 12; // 12h

	private static final WVWObjectiveNameDTO[] EMPTY_OBJECTIVE_NAME_ARRAY = new WVWObjectiveNameDTO[0];
	private static final URL MATCHES_URL;
	private static final URL MATCH_DETAILS_URL;
	private static final URL OBJECTIVE_NAMES_URL;
	static {
		try {
			MATCHES_URL = new URL("https://api.guildwars2.com/" + ArenanetUtils.API_VERSION + "/wvw/matches.json");
			MATCH_DETAILS_URL = new URL("https://api.guildwars2.com/" + ArenanetUtils.API_VERSION + "/wvw/match_details.json");
			OBJECTIVE_NAMES_URL = new URL("https://api.guildwars2.com/" + ArenanetUtils.API_VERSION + "/wvw/objective_names.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}
	}

	// caches
	private final Cache<Locale, Optional<WVWObjectiveNameDTO[]>> objectiveNamesCache = CacheBuilder.newBuilder()
			.expireAfterWrite(OBJECTIVE_NAMES_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).removalListener(new RemovalListener<Locale, Optional<WVWObjectiveNameDTO[]>>() {
				@Override
				public void onRemoval(final RemovalNotification<Locale, Optional<WVWObjectiveNameDTO[]>> notification) {
					// synchronize
					// objectiveNamesCache and
					// objectiveNameCaches
					if (DefaultWVWService.this.objectiveNameCaches.containsKey(notification.getKey())) {
						DefaultWVWService.this.objectiveNameCaches.get(notification.getKey()).invalidateAll();
					}
				}
			}).build();
	private final Map<Locale, Cache<Integer, Optional<WVWObjectiveNameDTO>>> objectiveNameCaches = new HashMap<Locale, Cache<Integer, Optional<WVWObjectiveNameDTO>>>();
	private final Cache<String, Optional<WVWMatchDetailsDTO>> matchDetailsCache = CacheBuilder.newBuilder()
			.expireAfterWrite(MATCH_DETAILS_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build();
	private final Cache<String, Optional<WVWMatchesDTO>> matchesCache = CacheBuilder.newBuilder().initialCapacity(1).maximumSize(1)
			.expireAfterWrite(MATCH_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).removalListener(new RemovalListener<String, Optional<WVWMatchesDTO>>() {
				@Override
				public void onRemoval(final RemovalNotification<String, Optional<WVWMatchesDTO>> notification) {
					// synchronize matchesCache and
					// matchCache
					DefaultWVWService.this.matchCache.invalidateAll();
				}
			}).build();
	private final Cache<String, Optional<WVWMatchDTO>> matchCache = CacheBuilder.newBuilder().expireAfterWrite(MATCH_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build();

	// injections
	private final WVWDTOFactory wvwDTOFactory;

	@Inject
	public DefaultWVWService(final WVWDTOFactory wvwDTOFactory) {
		checkNotNull(wvwDTOFactory);
		this.wvwDTOFactory = wvwDTOFactory;
	}

	/**
	 * get or create a locale specific cache for {@link WVWObjectiveNameDTO}s
	 *
	 * @param locale
	 * @return
	 */
	private Cache<Integer, Optional<WVWObjectiveNameDTO>> getOrCreateObjectiveNameCache(final Locale locale) {
		checkNotNull(locale);
		final Locale key = ArenanetUtils.normalizeLocaleForAPIUsage(locale);
		if (!this.objectiveNameCaches.containsKey(key)) {
			synchronized (this) {
				if (!this.objectiveNameCaches.containsKey(key)) {
					final Cache<Integer, Optional<WVWObjectiveNameDTO>> newCache = CacheBuilder.newBuilder()
							.expireAfterWrite(OBJECTIVE_NAMES_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build();
					this.objectiveNameCaches.put(key, newCache);
				}
			}
		}
		checkState(this.objectiveNameCaches.containsKey(key));
		return this.objectiveNameCaches.get(key);
	}

	// TODO refactor this to return optional
	@Override
	public WVWMatchesDTO retrieveAllMatches() {
		try {
			return this.matchesCache.get("", new Callable<Optional<WVWMatchesDTO>>() {
				@Override
				public Optional<WVWMatchesDTO> call() throws Exception {
					final WebResource resource = ArenanetUtils.REST_CLIENT.resource(MATCHES_URL.toExternalForm());
					resource.addFilter(new RetryClientFilter(ArenanetUtils.REST_RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response={}", response);
						final WVWMatchesDTO result = DefaultWVWService.this.wvwDTOFactory.newMatchesOf(response);
						LOGGER.debug("Built result={}", result);
						return Optional.of(result);
					} catch (ClientHandlerException | UniformInterfaceException e) {
						LOGGER.error("Exception thrown while quering {}", resource, e);
						return Optional.absent();
					}
				}
			}).orNull();
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve {} from cache.", WVWMatchesDTO.class, e);
			throw new IllegalStateException("Failed to retrieve " + WVWMatchesDTO.class.getSimpleName() + " from cache.", e);
		}
	}

	@Override
	public Optional<WVWMatchDetailsDTO> retrieveMatchDetails(final String id) {
		checkNotNull(id);
		try {
			return this.matchDetailsCache.get(id, new Callable<Optional<WVWMatchDetailsDTO>>() {
				@Override
				public Optional<WVWMatchDetailsDTO> call() throws Exception {
					final WebResource resource = ArenanetUtils.REST_CLIENT.resource(MATCH_DETAILS_URL.toExternalForm()).queryParam("match_id", id);
					resource.addFilter(new RetryClientFilter(ArenanetUtils.REST_RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response={}", response);
						final WVWMatchDetailsDTO result = DefaultWVWService.this.wvwDTOFactory.newMatchDetailsOf(response);
						LOGGER.debug("Built result={}", result);
						return Optional.of(result);
					} catch (ClientHandlerException | UniformInterfaceException e) {
						LOGGER.error("Exception thrown while quering {}", resource, e);
						return Optional.absent();
					}
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve " + WVWMatchDetailsDTO.class.getSimpleName() + " from cache for id=" + id, e);
			throw new IllegalStateException("Failed to retrieve " + WVWMatchDetailsDTO.class.getSimpleName() + " from cache for id=" + id, e);
		}
	}

	// TODO refactor this to return optional
	@Override
	public WVWObjectiveNameDTO[] retrieveAllObjectiveNames(final Locale locale) {
		checkNotNull(locale);
		final Locale key = ArenanetUtils.normalizeLocaleForAPIUsage(locale);
		try {
			return this.objectiveNamesCache.get(key, new Callable<Optional<WVWObjectiveNameDTO[]>>() {
				@Override
				public Optional<WVWObjectiveNameDTO[]> call() throws Exception {
					final WebResource resource = ArenanetUtils.REST_CLIENT.resource(OBJECTIVE_NAMES_URL.toExternalForm()).queryParam("lang", key.getLanguage());
					resource.addFilter(new RetryClientFilter(ArenanetUtils.REST_RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response={}", response);
						final WVWObjectiveNameDTO[] result = DefaultWVWService.this.wvwDTOFactory.newObjectiveNamesOf(response);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Built result={}", Arrays.deepToString(result));
						}
						return Optional.of(result);
					} catch (ClientHandlerException | UniformInterfaceException e) {
						LOGGER.error("Exception thrown while quering {}", resource, e);
						return Optional.absent();
					}
				}
			}).or(EMPTY_OBJECTIVE_NAME_ARRAY);
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve all " + WVWObjectiveNameDTO.class.getSimpleName() + " from cache for lang=" + key, e);
			throw new IllegalStateException("Failed to retrieve all " + WVWObjectiveNameDTO.class.getSimpleName() + " from cache for lang=" + key, e);
		}
	}

	@Override
	public Optional<WVWObjectiveNameDTO> retrieveObjectiveName(final Locale locale, final int id) {
		checkNotNull(locale);
		checkArgument(id > 0);
		final Locale key = ArenanetUtils.normalizeLocaleForAPIUsage(locale);
		try {
			// retrieve value from cache
			return this.getOrCreateObjectiveNameCache(key).get(id, new Callable<Optional<WVWObjectiveNameDTO>>() {
				@Override
				public Optional<WVWObjectiveNameDTO> call() throws Exception {
					final WVWObjectiveNameDTO[] names = DefaultWVWService.this.retrieveAllObjectiveNames(key);
					int index = 0;
					WVWObjectiveNameDTO result = null;
					while ((index < names.length) && (result == null)) {
						result = names[index].getId() == id ? names[index] : null;
						index++;
					}
					LOGGER.trace("Retrieved {} for id={} and lang={}: {}", WVWObjectiveNameDTO.class, id, key, result);
					return Optional.fromNullable(result);
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve {} from cache for id={} lang={}", WorldNameDTO.class, id, key, e);
			throw new IllegalStateException("Failed to retrieve all " + WorldNameDTO.class.getSimpleName() + " from cache for worldId=" + id + " lang=" + locale, e);
		}

	}

	@Override
	public Optional<WVWMatchDTO> retrieveMatch(final String matchId) {
		checkNotNull(matchId);
		try {
			// retrieve value from cache
			return this.matchCache.get(matchId, new Callable<Optional<WVWMatchDTO>>() {
				@Override
				public Optional<WVWMatchDTO> call() throws Exception {
					final WVWMatchDTO[] matches = DefaultWVWService.this.retrieveAllMatches().getMatches();
					int index = 0;
					WVWMatchDTO result = null;
					while ((index < matches.length) && (result == null)) {
						result = matches[index].getId().equals(matchId) ? matches[index] : null;
						index++;
					}
					LOGGER.trace("Retrieved {} for matchId={}: {}", WVWMatchDTO.class, matchId, result);
					return Optional.fromNullable(result);
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve {} from cache for matchId={}", WVWMatchDTO.class, matchId, e);
			throw new IllegalStateException("Failed to retrieve all " + WVWMatchDTO.class.getSimpleName() + " from cache for matchId=" + matchId, e);
		}
	}

}
