package de.justi.yagw2api.arenanet.impl;

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

import de.justi.yagw2api.arenanet.IWVWDTOFactory;
import de.justi.yagw2api.arenanet.IWorldDTOFactory;
import de.justi.yagw2api.arenanet.IWorldNameDTO;
import de.justi.yagw2api.arenanet.IWorldService;
import de.justi.yagwapi.common.utils.RetryClientFilter;

final class WorldService implements IWorldService {
	private static final Logger LOGGER = Logger.getLogger(WorldService.class);
	private static final long WOLRD_NAMES_CACHE_EXPIRE_MILLIS = 1000 * 60 * 60 * 12; // 12h
	private static final URL WORL_NAMES_URL;
	static {
		try {
			WORL_NAMES_URL = new URL("https://api.guildwars2.com/" + ServiceUtils.API_VERSION + "/world_names.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}

	}

	// FIELDS
	private final Cache<Locale, IWorldNameDTO[]> worldNamesCache = CacheBuilder.newBuilder().expireAfterWrite(WOLRD_NAMES_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS)
			.removalListener(new RemovalListener<Locale, IWorldNameDTO[]>() {
				@Override
				public void onRemoval(RemovalNotification<Locale, IWorldNameDTO[]> notification) {
					// synchronize worldNamesCache
					// and worldNameCaches
					if (WorldService.this.worldNameCaches.containsKey(notification.getKey())) {
						WorldService.this.worldNameCaches.get(notification.getKey()).invalidateAll();
					}
				}
			}).build();
	private final Map<Locale, Cache<Integer, IWorldNameDTO>> worldNameCaches = new HashMap<Locale, Cache<Integer, IWorldNameDTO>>();
	private final IWVWDTOFactory wvwDTOFactory;
	private final IWorldDTOFactory worldDTOFactory;

	// METHODS
	@Inject
	public WorldService(IWVWDTOFactory wvwDTOFactory, IWorldDTOFactory worldDTOFactory) {
		this.wvwDTOFactory = checkNotNull(wvwDTOFactory);
		this.worldDTOFactory = checkNotNull(worldDTOFactory);
	}

	/**
	 * get or create a locale specific cache for {@link IWorldNameDTO}s
	 * 
	 * @param locale
	 * @return
	 */
	private Cache<Integer, IWorldNameDTO> getOrCreateWorldNameCache(Locale locale) {
		checkNotNull(locale);
		final Locale key = ServiceUtils.normalizeLocaleForAPIUsage(locale);
		if (!this.worldNameCaches.containsKey(key)) {
			synchronized (this) {
				if (!this.worldNameCaches.containsKey(key)) {
					final Cache<Integer, IWorldNameDTO> newCache = CacheBuilder.newBuilder().expireAfterWrite(WOLRD_NAMES_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build();
					this.worldNameCaches.put(key, newCache);
				}
			}
		}
		checkState(this.worldNameCaches.containsKey(key));
		return this.worldNameCaches.get(key);
	}

	@Override
	public IWorldNameDTO[] retrieveAllWorldNames(final Locale locale) {
		checkNotNull(locale);
		final Locale key = ServiceUtils.normalizeLocaleForAPIUsage(locale);
		try {
			return this.worldNamesCache.get(key, new Callable<IWorldNameDTO[]>() {
				@Override
				public IWorldNameDTO[] call() throws Exception {
					checkNotNull(key);
					final WebResource resource = ServiceUtils.REST_CLIENT.resource(WORL_NAMES_URL.toExternalForm()).queryParam("lang", key.getLanguage());
					resource.addFilter(new RetryClientFilter(ServiceUtils.REST_RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response=" + response);
						final IWorldNameDTO[] result = WorldService.this.worldDTOFactory.newWorldNamesOf(response);
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

	@Override
	public Optional<IWorldNameDTO> retrieveWorldName(final Locale locale, final int worldId) {
		checkNotNull(locale);
		checkArgument(worldId > 0);
		final Locale key = ServiceUtils.normalizeLocaleForAPIUsage(locale);
		try {
			// retrieve value from cache
			return Optional.fromNullable(this.getOrCreateWorldNameCache(key).get(worldId, new Callable<IWorldNameDTO>() {
				@Override
				public IWorldNameDTO call() throws Exception {
					final IWorldNameDTO[] names = WorldService.this.retrieveAllWorldNames(key);
					int index = 0;
					IWorldNameDTO result = null;
					while ((index < names.length) && (result == null)) {
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
			throw new IllegalStateException("Failed to retrieve all " + IWorldNameDTO.class.getSimpleName() + " from cache for worldId=" + worldId + " lang=" + locale, e);
		}
	}
}
