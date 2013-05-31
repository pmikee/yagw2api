package de.justi.yagw2api.core.arenanet.service.impl;

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

import org.apache.log4j.Logger;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;

import de.justi.yagw2api.core.arenanet.dto.IWVWDTOFactory;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDetailsDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchesDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWObjectiveNameDTO;
import de.justi.yagw2api.core.arenanet.dto.IWorldNameDTO;
import de.justi.yagw2api.core.arenanet.service.IWVWService;

class WVWService extends AbstractService implements IWVWService {
	private static final int										RETRY_COUNT							= 10;
	private static final long										MATCH_CACHE_EXPIRE_MILLIS			= 1000 * 60 * 10;												// 10m
	private static final long										MATCH_DETAILS_CACHE_EXPIRE_MILLIS	= 1000 * 3;													// 3s
	private static final long										WOLRD_NAMES_CACHE_EXPIRE_MILLIS		= 1000 * 60 * 60 * 12;											// 12h
	private static final long										OBJECTIVE_NAMES_CACHE_EXPIRE_MILLIS	= 1000 * 60 * 60 * 12;											// 12h

	private static final String										API_VERSION							= "v1";
	private static final Logger										LOGGER								= Logger.getLogger(WVWService.class);

	private static final URL										MATCHES_URL;
	private static final URL										MATCH_DETAILS_URL;
	private static final URL										OBJECTIVE_NAMES_URL;
	private static final URL										WORL_NAMES_URL;
	static {
		try {
			MATCHES_URL = new URL("https://api.guildwars2.com/" + API_VERSION + "/wvw/matches.json");
			MATCH_DETAILS_URL = new URL("https://api.guildwars2.com/" + API_VERSION + "/wvw/match_details.json");
			OBJECTIVE_NAMES_URL = new URL("https://api.guildwars2.com/" + API_VERSION + "/wvw/objective_names.json");
			WORL_NAMES_URL = new URL("https://api.guildwars2.com/" + API_VERSION + "/world_names.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}
	}

	// caches
	private final Cache<Locale, IWorldNameDTO[]>					worldNamesCache						= CacheBuilder
																												.newBuilder()
																												.expireAfterWrite(
																														WOLRD_NAMES_CACHE_EXPIRE_MILLIS,
																														TimeUnit.MILLISECONDS)
																												.removalListener(
																														new RemovalListener<Locale, IWorldNameDTO[]>() {
																															public void onRemoval(
																																	RemovalNotification<Locale, IWorldNameDTO[]> notification) {
																																// synchronize worldNamesCache
																																// and worldNameCaches
																																if (WVWService.this.worldNameCaches
																																		.containsKey(notification
																																				.getKey())) {
																																	WVWService.this.worldNameCaches
																																			.get(notification
																																					.getKey())
																																			.invalidateAll();
																																}
																															}
																														}).build();
	private final Map<Locale, Cache<Integer, IWorldNameDTO>>		worldNameCaches						= new HashMap<Locale, Cache<Integer, IWorldNameDTO>>();
	private final Cache<Locale, IWVWObjectiveNameDTO[]>				objectiveNamesCache					= CacheBuilder
																												.newBuilder()
																												.expireAfterWrite(
																														OBJECTIVE_NAMES_CACHE_EXPIRE_MILLIS,
																														TimeUnit.MILLISECONDS)
																												.removalListener(
																														new RemovalListener<Locale, IWVWObjectiveNameDTO[]>() {
																															public void onRemoval(
																																	RemovalNotification<Locale, IWVWObjectiveNameDTO[]> notification) {
																																// synchronize
																																// objectiveNamesCache and
																																// objectiveNameCaches
																																if (WVWService.this.objectiveNameCaches
																																		.containsKey(notification
																																				.getKey())) {
																																	WVWService.this.objectiveNameCaches
																																			.get(notification
																																					.getKey())
																																			.invalidateAll();
																																}
																															}
																														}).build();
	private final Map<Locale, Cache<Integer, IWVWObjectiveNameDTO>>	objectiveNameCaches					= new HashMap<Locale, Cache<Integer, IWVWObjectiveNameDTO>>();
	private final Cache<String, IWVWMatchDetailsDTO>				matchDetailsCache					= CacheBuilder
																												.newBuilder()
																												.expireAfterWrite(
																														MATCH_DETAILS_CACHE_EXPIRE_MILLIS,
																														TimeUnit.MILLISECONDS).build();
	private final Cache<String, IWVWMatchesDTO>						matchesCache						= CacheBuilder
																												.newBuilder()
																												.initialCapacity(1)
																												.maximumSize(1)
																												.expireAfterWrite(MATCH_CACHE_EXPIRE_MILLIS,
																														TimeUnit.MILLISECONDS)
																												.removalListener(
																														new RemovalListener<String, IWVWMatchesDTO>() {
																															public void onRemoval(
																																	RemovalNotification<String, IWVWMatchesDTO> notification) {
																																// synchronize matchesCache and
																																// matchCache
																																WVWService.this.matchCache
																																		.invalidateAll();
																															}
																														}).build();
	private final Cache<String, IWVWMatchDTO>						matchCache							= CacheBuilder
																												.newBuilder()
																												.expireAfterWrite(MATCH_CACHE_EXPIRE_MILLIS,
																														TimeUnit.MILLISECONDS).build();

	// injections
	private IWVWDTOFactory											wvwDTOFactory;

	@Inject
	public WVWService(IWVWDTOFactory wvwDTOFactory) {
		checkNotNull(wvwDTOFactory);
		this.wvwDTOFactory = wvwDTOFactory;
	}

	/**
	 * get or create a locale specific cache for {@link IWorldNameDTO}s
	 * 
	 * @param locale
	 * @return
	 */
	private Cache<Integer, IWorldNameDTO> getOrCreateWorldNameCache(Locale locale) {
		checkNotNull(locale);
		final Locale key = Locale.forLanguageTag(locale.getLanguage());
		if (!this.objectiveNameCaches.containsKey(key)) {
			synchronized (this) {
				if (!this.worldNameCaches.containsKey(key)) {
					final Cache<Integer, IWorldNameDTO> newCache = CacheBuilder.newBuilder()
							.expireAfterWrite(WOLRD_NAMES_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build();
					this.worldNameCaches.put(key, newCache);
				}
			}
		}
		checkState(this.worldNameCaches.containsKey(key));
		return this.worldNameCaches.get(key);
	}

	/**
	 * get or create a locale specific cache for {@link IWVWObjectiveNameDTO}s
	 * 
	 * @param locale
	 * @return
	 */
	private Cache<Integer, IWVWObjectiveNameDTO> getOrCreateObjectiveNameCache(Locale locale) {
		checkNotNull(locale);
		final Locale key = Locale.forLanguageTag(locale.getLanguage());
		if (!this.objectiveNameCaches.containsKey(key)) {
			synchronized (this) {
				if (!this.objectiveNameCaches.containsKey(key)) {
					final Cache<Integer, IWVWObjectiveNameDTO> newCache = CacheBuilder.newBuilder()
							.expireAfterWrite(OBJECTIVE_NAMES_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build();
					this.objectiveNameCaches.put(key, newCache);
				}
			}
		}
		checkState(this.objectiveNameCaches.containsKey(key));
		return this.objectiveNameCaches.get(key);
	}

	public IWVWMatchesDTO retrieveAllMatches() {
		try {
			return this.matchesCache.get("", new Callable<IWVWMatchesDTO>() {
				public IWVWMatchesDTO call() throws Exception {
					final WebResource resource = CLIENT.resource(MATCHES_URL.toExternalForm());
					resource.addFilter(new RetryClientFilter(RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response=" + response);
						final IWVWMatchesDTO result = WVWService.this.wvwDTOFactory.newMatchesOf(response, WVWService.this);
						LOGGER.debug("Built result=" + result);
						return result;
					} catch (ClientHandlerException e) {
						LOGGER.fatal("Exception thrown while quering " + resource.getURI(), e);
						return null;
					}
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve " + IWVWMatchesDTO.class.getSimpleName() + " from cache.", e);
			throw new IllegalStateException("Failed to retrieve " + IWVWMatchesDTO.class.getSimpleName() + " from cache.", e);
		}
	}

	public IWVWMatchDetailsDTO retrieveMatchDetails(final String id) {
		checkNotNull(id);
		try {
			return this.matchDetailsCache.get(id, new Callable<IWVWMatchDetailsDTO>() {
				public IWVWMatchDetailsDTO call() throws Exception {
					final WebResource resource = CLIENT.resource(MATCH_DETAILS_URL.toExternalForm()).queryParam("match_id", id);
					resource.addFilter(new RetryClientFilter(RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);						
						LOGGER.trace("Retrieved response=" + response);
						final IWVWMatchDetailsDTO result = WVWService.this.wvwDTOFactory.newMatchDetailsOf(response, WVWService.this);
						LOGGER.debug("Built result=" + result);
						return result;
					} catch (ClientHandlerException e) {
						LOGGER.fatal("Exception thrown while quering " + resource.getURI(), e);
						return null;
					}
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve all " + IWVWMatchDetailsDTO.class.getSimpleName() + " from cache for id=" + id, e);
			throw new IllegalStateException("Failed to retrieve all " + IWVWMatchDetailsDTO.class.getSimpleName() + " from cache for id=" + id, e);
		}
	}

	public IWVWObjectiveNameDTO[] retrieveAllObjectiveNames(final Locale locale) {
		checkNotNull(locale);
		final Locale key = Locale.forLanguageTag(locale.getLanguage());
		try {
			return this.objectiveNamesCache.get(key, new Callable<IWVWObjectiveNameDTO[]>() {
				public IWVWObjectiveNameDTO[] call() throws Exception {
					final WebResource resource = CLIENT.resource(OBJECTIVE_NAMES_URL.toExternalForm()).queryParam("lang", key.getLanguage());
					resource.addFilter(new RetryClientFilter(RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response=" + response);
						final IWVWObjectiveNameDTO[] result = WVWService.this.wvwDTOFactory.newObjectiveNamesOf(response, WVWService.this);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Built result=" + Arrays.deepToString(result));
						}
						return result;
					} catch (ClientHandlerException e) {
						LOGGER.fatal("Exception thrown while quering " + resource.getURI(), e);
						return null;
					}
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve all " + IWVWObjectiveNameDTO.class.getSimpleName() + " from cache for lang=" + key, e);
			throw new IllegalStateException("Failed to retrieve all " + IWVWObjectiveNameDTO.class.getSimpleName() + " from cache for lang=" + key, e);
		}
	}

	public IWorldNameDTO[] retrieveAllWorldNames(final Locale locale) {
		checkNotNull(locale);
		final Locale key = Locale.forLanguageTag(locale.getLanguage());
		try {
			return this.worldNamesCache.get( key, new Callable<IWorldNameDTO[]>() {
				public IWorldNameDTO[] call() throws Exception {
					checkNotNull(key);
					final WebResource resource = CLIENT.resource(WORL_NAMES_URL.toExternalForm()).queryParam("lang",  key.getLanguage());
					resource.addFilter(new RetryClientFilter(RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response=" + response);
						final IWorldNameDTO[] result = WVWService.this.wvwDTOFactory.newWorldNamesOf(response, WVWService.this);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Built result=" + Arrays.deepToString(result));
						}
						return result;
					} catch (ClientHandlerException e) {
						LOGGER.fatal("Exception thrown while quering " + resource.getURI(), e);
						return null;
					}
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve all " + IWorldNameDTO.class.getSimpleName() + " from cache for lang=" + locale, e);
			throw new IllegalStateException("Failed to retrieve all " + IWorldNameDTO.class.getSimpleName() + " from cache for lang=" + locale, e);
		}
	}

	public Optional<IWorldNameDTO> retrieveWorldName(final Locale locale, final int worldId) {
		checkNotNull(locale);
		checkArgument(worldId > 0);
		final Locale key = Locale.forLanguageTag(locale.getLanguage());
		try {
			// retrieve value from cache
			return Optional.fromNullable(this.getOrCreateWorldNameCache(key).get(worldId, new Callable<IWorldNameDTO>() {
				public IWorldNameDTO call() throws Exception {
					final IWorldNameDTO[] names = WVWService.this.retrieveAllWorldNames(key);
					int index = 0;
					IWorldNameDTO result = null;
					while (index < names.length && result == null) {
						result = names[index].getId() == worldId ? names[index] : null;
						index++;
					}
					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace("Retrieved " + IWorldNameDTO.class.getSimpleName() + " for worldId=" + worldId + " and lang=" + locale + ": " + result);
					}
					return result;
				}
			}));
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve " + IWorldNameDTO.class.getSimpleName() + " from cache for worldId=" + worldId + " lang=" + locale, e);
			throw new IllegalStateException("Failed to retrieve all " + IWorldNameDTO.class.getSimpleName() + " from cache for worldId=" + worldId + " lang="
					+ locale, e);
		}
	}

	public Optional<IWVWObjectiveNameDTO> retrieveObjectiveName(final Locale locale, final int id) {
		checkNotNull(locale);
		checkArgument(id > 0);
		final Locale key = Locale.forLanguageTag(locale.getLanguage());
		try {
			// retrieve value from cache
			return Optional.fromNullable(this.getOrCreateObjectiveNameCache(key).get(id, new Callable<IWVWObjectiveNameDTO>() {
				public IWVWObjectiveNameDTO call() throws Exception {
					final IWVWObjectiveNameDTO[] names = WVWService.this.retrieveAllObjectiveNames(key);
					int index = 0;
					IWVWObjectiveNameDTO result = null;
					while (index < names.length && result == null) {
						result = names[index].getId() == id ? names[index] : null;
						index++;
					}
					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace("Retrieved " + IWVWObjectiveNameDTO.class.getSimpleName() + " for id=" + id + " and lang=" + key + ": " + result);
					}
					return result;
				}
			}));
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve " + IWorldNameDTO.class.getSimpleName() + " from cache for id=" + id + " lang=" + key, e);
			throw new IllegalStateException("Failed to retrieve all " + IWorldNameDTO.class.getSimpleName() + " from cache for worldId=" + id + " lang="
					+ locale, e);
		}

	}

	public Optional<IWVWMatchDTO> retrieveMatch(final String matchId) {
		checkNotNull(matchId);
		try {
			// retrieve value from cache
			return Optional.fromNullable(this.matchCache.get(matchId, new Callable<IWVWMatchDTO>() {
				public IWVWMatchDTO call() throws Exception {
					final IWVWMatchDTO[] matches = WVWService.this.retrieveAllMatches().getMatches();
					int index = 0;
					IWVWMatchDTO result = null;
					while (index < matches.length && result == null) {
						result = matches[index].getId().equals(matchId) ? matches[index] : null;
						index++;
					}
					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace("Retrieved " + IWVWMatchDTO.class.getSimpleName() + " for matchId=" + matchId + ": " + result);
					}
					return result;
				}
			}));
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve " + IWVWMatchDTO.class.getSimpleName() + " from cache for matchId=" + matchId, e);
			throw new IllegalStateException("Failed to retrieve all " + IWVWMatchDTO.class.getSimpleName() + " from cache for matchId=" + matchId, e);
		}
	}

}
