package de.justi.yagw2api.arenanet.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
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
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import de.justi.yagw2api.arenanet.IWorldDTOFactory;
import de.justi.yagw2api.arenanet.IWorldNameDTO;
import de.justi.yagw2api.arenanet.IWorldService;
import de.justi.yagwapi.common.RetryClientFilter;

final class WorldService implements IWorldService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldService.class);
	private static final long WOLRD_NAMES_CACHE_EXPIRE_MILLIS = 1000 * 60 * 60 * 12; // 12h
	private static final IWorldNameDTO[] EMPTY_WORLD_NAME_ARRAY = new IWorldNameDTO[0];
	private static final URL WORL_NAMES_URL;
	static {
		try {
			WORL_NAMES_URL = new URL("https://api.guildwars2.com/" + ServiceUtils.API_VERSION + "/world_names.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}

	}

	// FIELDS
	private final Cache<Locale, Optional<IWorldNameDTO[]>> worldNamesCache = CacheBuilder.newBuilder().expireAfterWrite(WOLRD_NAMES_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS)
			.removalListener(new RemovalListener<Locale, Optional<IWorldNameDTO[]>>() {
				@Override
				public void onRemoval(RemovalNotification<Locale, Optional<IWorldNameDTO[]>> notification) {
					// synchronize worldNamesCache with worldNameCaches
					if (WorldService.this.worldNameCaches.containsKey(notification.getKey())) {
						WorldService.this.worldNameCaches.get(notification.getKey()).invalidateAll();
					}
				}
			}).build();
	private final Map<Locale, Cache<Integer, Optional<IWorldNameDTO>>> worldNameCaches = Maps.newHashMap();
	private final IWorldDTOFactory worldDTOFactory;

	// METHODS
	@Inject
	public WorldService(IWorldDTOFactory worldDTOFactory) {
		this.worldDTOFactory = checkNotNull(worldDTOFactory);
	}

	/**
	 * get or create a locale specific cache for {@link IWorldNameDTO}s
	 * 
	 * @param locale
	 * @return
	 */
	private Cache<Integer, Optional<IWorldNameDTO>> getOrCreateWorldNameCache(Locale locale) {
		checkNotNull(locale);
		final Locale key = ServiceUtils.normalizeLocaleForAPIUsage(locale);
		if (!this.worldNameCaches.containsKey(key)) {
			synchronized (this) {
				if (!this.worldNameCaches.containsKey(key)) {
					final Cache<Integer, Optional<IWorldNameDTO>> newCache = CacheBuilder.newBuilder().expireAfterWrite(WOLRD_NAMES_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build();
					this.worldNameCaches.put(key, newCache);
				}
			}
		}
		checkState(this.worldNameCaches.containsKey(key));
		return this.worldNameCaches.get(key);
	}

	// TODO refactor this to return optional
	@Override
	public IWorldNameDTO[] retrieveAllWorldNames(final Locale locale) {
		checkNotNull(locale);
		final Locale key = ServiceUtils.normalizeLocaleForAPIUsage(locale);
		try {
			return this.worldNamesCache.get(key, new Callable<Optional<IWorldNameDTO[]>>() {
				@Override
				public Optional<IWorldNameDTO[]> call() throws Exception {
					checkNotNull(key);
					final WebResource resource = ServiceUtils.REST_CLIENT.resource(WORL_NAMES_URL.toExternalForm()).queryParam("lang", key.getLanguage());
					resource.addFilter(new RetryClientFilter(ServiceUtils.REST_RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response={}", response);
						final IWorldNameDTO[] result = WorldService.this.worldDTOFactory.newWorldNamesOf(response);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Built result={}", Arrays.deepToString(result));
						}
						return Optional.of(result);
					} catch (ClientHandlerException | UniformInterfaceException e) {
						LOGGER.error("Exception thrown while quering {}", resource, e);
						return Optional.absent();
					}
				}
			}).or(EMPTY_WORLD_NAME_ARRAY);
		} catch (ExecutionException e) {
			throw new Error("Failed to retrieve all " + IWorldNameDTO.class.getSimpleName() + " from cache for lang=" + locale, e);
		}
	}

	@Override
	public Optional<IWorldNameDTO> retrieveWorldName(final Locale locale, final int worldId) {
		checkNotNull(locale);
		checkArgument(worldId > 0);
		final Locale key = ServiceUtils.normalizeLocaleForAPIUsage(locale);
		try {
			// retrieve value from cache
			return this.getOrCreateWorldNameCache(key).get(worldId, new Callable<Optional<IWorldNameDTO>>() {
				@Override
				public Optional<IWorldNameDTO> call() throws Exception {
					final IWorldNameDTO[] names = WorldService.this.retrieveAllWorldNames(key);
					int index = 0;
					IWorldNameDTO result = null;
					while ((index < names.length) && (result == null)) {
						result = names[index].getId() == worldId ? names[index] : null;
						index++;
					}
					return Optional.fromNullable(result);
				}
			});
		} catch (ExecutionException e) {
			throw new Error("Failed to retrieve all " + IWorldNameDTO.class.getSimpleName() + " from cache for worldId=" + worldId + " lang=" + locale, e);
		}
	}
}
