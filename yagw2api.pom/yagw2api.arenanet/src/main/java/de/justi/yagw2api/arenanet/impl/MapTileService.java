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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;

import de.justi.yagw2api.arenanet.IMapTileService;

public class MapTileService implements IMapTileService {
	// CONSTS
	private static final Path RELATIVE_TEMP_DIR_PATH = Paths.get("yagw2api", MapTileService.class.getSimpleName());
	private static final Logger LOGGER = LoggerFactory.getLogger(MapTileService.class);

	// EMBEDDED
	@Immutable
	private static final class MapTileSelector {
		// CONSTS
		private static final int maxDNSAliasIndex = 4;

		// STATIC
		private static final AtomicInteger dnsAliasIndex = new AtomicInteger(1);
		private static final Map<String, MapTileSelector> instances = Maps.newConcurrentMap();

		public static final MapTileSelector of(final int continentId, final int floor, final int zoom, final int x, final int y) {
			final String key = String.valueOf(continentId) + floor + zoom + x + y;
			if (!instances.containsKey(key)) {
				synchronized (instances) {
					if (!instances.containsKey(key)) {
						instances.put(key, new MapTileSelector(continentId, floor, zoom, x, y));
					}
				}
			}
			return instances.get(key);
		}

		// FIELDS
		private final int continentId;
		private final int floor;
		private final int zoom;
		private final int x;
		private final int y;

		// CONSTRUCTOR
		private MapTileSelector(final int continentId, final int floor, final int zoom, final int x, final int y) {
			this.continentId = continentId;
			this.floor = floor;
			this.zoom = zoom;
			this.x = x;
			this.y = y;
		}

		// METHODS
		public final int getContinentId() {
			return this.continentId;
		}

		public final int getFloor() {
			return this.floor;
		}

		public final int getZoom() {
			return this.zoom;
		}

		public final int getX() {
			return this.x;
		}

		public final int getY() {
			return this.y;
		}

		public final URL toURL() {
			try {
				return new URL("https", "tiles" + dnsAliasIndex.getAndUpdate(new IntUnaryOperator() {

					@Override
					public int applyAsInt(final int operand) {
						return (operand % maxDNSAliasIndex) + 1;
					}
				}) + ".guildwars2.com", "/" + this.continentId + "/" + this.floor + "/" + this.zoom + "/" + this.x + "/" + this.y + ".jpg");
			} catch (MalformedURLException e) {
				throw new Error(e);
			}
		}

		@Override
		public final String toString() {
			return MoreObjects.toStringHelper(this).add("continentId", this.continentId).add("floor", this.floor).add("zoom", this.zoom).add("x", this.x).add("y", this.y)
					.toString();
		}
	}

	private final LoadingCache<MapTileSelector, Optional<Path>> mapTileCache = CacheBuilder.newBuilder().build(new CacheLoader<MapTileSelector, Optional<Path>>() {
		@Override
		public Optional<Path> load(final MapTileSelector key) throws Exception {
			checkNotNull(key, "missing key");
			final URL url = key.toURL();
			try {
				final Path dir = de.justi.yagwapi.common.Files.getTempDir().resolve(RELATIVE_TEMP_DIR_PATH);
				final Path file = dir.resolve(key.getContinentId() + "." + key.getFloor() + "." + key.getZoom() + "." + key.getX() + "." + key.getY() + ".jpg");
				if (!Files.exists(file)) {
					try (final InputStream in = url.openStream()) {
						Files.createDirectories(dir);
						LOGGER.trace("going to download {} to {}", url, file);
						java.nio.file.Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
						LOGGER.debug("downloaded {} to {}", url, file);
					}
				}
				return Optional.of(file);
			} catch (IOException e) {
				LOGGER.warn("unable to download {}", url);
				return Optional.absent();
			}
		}
	});

	@Override
	public Optional<Path> getMapTile(final int continentId, final int floor, final int zoom, final int x, final int y) {
		final MapTileSelector selector = MapTileSelector.of(continentId, floor, zoom, x, y);
		try {
			return this.mapTileCache.get(selector);
		} catch (ExecutionException e) {
			throw new Error(e);
		}
	}
}
