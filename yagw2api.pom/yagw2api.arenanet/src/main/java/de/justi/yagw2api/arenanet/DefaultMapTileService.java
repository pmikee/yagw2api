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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import de.justi.yagw2api.common.tuple.Tuple5;
import de.justi.yagw2api.common.tuple.Tuples;

final class DefaultMapTileService implements MapTileService {
	// CONSTS
	private static final Path RELATIVE_TEMP_DIR_PATH = Paths.get("yagw2api", DefaultMapTileService.class.getSimpleName());
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMapTileService.class);
	private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setDaemon(true).setNameFormat(DefaultMapTileService.class.getSimpleName() + "-%d")
			.setUncaughtExceptionHandler((t, e) -> LOGGER.error("Uncaught exception in {}", t, e)).build();

	// TODO inject EXECUTOR_SERVICE for DefaultMapTileService
	private static final int THREAD_COUNT_PER_PROCESSOR = 5;
	private static final ListeningExecutorService EXECUTOR_SERVICE = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()
			* THREAD_COUNT_PER_PROCESSOR, THREAD_FACTORY));

	// EMBEDDED
	@Immutable
	private static final class MapTileSelector {
		// CONSTS
		private static final int maxDNSAliasIndex = 4;

		// STATIC
		private static final AtomicInteger dnsAliasIndex = new AtomicInteger(1);
		private static final LoadingCache<Tuple5<String, String, Integer, Integer, Integer>, MapTileSelector> instanceCache = CacheBuilder.newBuilder().build(
				new CacheLoader<Tuple5<String, String, Integer, Integer, Integer>, MapTileSelector>() {
					@Override
					public MapTileSelector load(final Tuple5<String, String, Integer, Integer, Integer> key) throws Exception {
						return new MapTileSelector(key.v1(), key.v2(), key.v3(), key.v4(), key.v5());
					}
				});

		public static final MapTileSelector of(final String continentId, final String floorIndex, final int zoom, final int x, final int y) {
			final Tuple5<String, String, Integer, Integer, Integer> key = Tuples.of(continentId, floorIndex, zoom, x, y);
			try {
				return instanceCache.get(key);
			} catch (ExecutionException e) {
				throw new Error(e);
			}
		}

		// FIELDS
		private final String continentId;
		private final String floorIndex;
		private final int zoom;
		private final int x;
		private final int y;

		// CONSTRUCTOR
		private MapTileSelector(final String continentId, final String floorIndex, final int zoom, final int x, final int y) {
			this.continentId = checkNotNull(continentId, "missing continentId");
			this.floorIndex = checkNotNull(floorIndex, "missing floorIndex");
			this.zoom = zoom;
			this.x = x;
			this.y = y;
		}

		// METHODS
		public final String getContinentId() {
			return this.continentId;
		}

		public final String getFloorIndex() {
			return this.floorIndex;
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
				return new URL("https", "tiles" + dnsAliasIndex.getAndUpdate(operand -> (operand % maxDNSAliasIndex) + 1) + ".guildwars2.com", "/" + this.continentId + "/"
						+ this.floorIndex + "/" + this.zoom + "/" + this.x + "/" + this.y + ".jpg");
			} catch (MalformedURLException e) {
				throw new Error(e);
			}
		}

		@Override
		public final String toString() {
			return MoreObjects.toStringHelper(this).add("continentId", this.continentId).add("floorIndex", this.floorIndex).add("zoom", this.zoom).add("x", this.x)
					.add("y", this.y).toString();
		}
	}

	private final LoadingCache<MapTileSelector, Optional<Path>> mapTileCache = CacheBuilder.newBuilder().build(new CacheLoader<MapTileSelector, Optional<Path>>() {
		@Override
		public Optional<Path> load(final MapTileSelector key) throws Exception {
			checkNotNull(key, "missing key");
			final URL url = key.toURL();
			try {
				final Path dir = de.justi.yagw2api.common.io.Files.getTempDir().resolve(RELATIVE_TEMP_DIR_PATH);
				final Path file = dir.resolve(key.getContinentId() + "." + key.getFloorIndex() + "." + key.getZoom() + "." + key.getX() + "." + key.getY() + ".jpg");
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
	public Optional<Path> getMapTile(final String continentId, final String floorIndex, final int zoom, final int x, final int y) {
		checkNotNull(continentId, "missing continentId");
		checkNotNull(floorIndex, "missing floorIndex");
		try {
			return this.getMapTileAsync(continentId, floorIndex, zoom, x, y).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new Error(e);
		}
	}

	@Override
	public ListenableFuture<Optional<Path>> getMapTileAsync(final String continentId, final String floorIndex, final int zoom, final int x, final int y) {
		checkNotNull(continentId, "missing continentId");
		checkNotNull(floorIndex, "missing floorIndex");
		return EXECUTOR_SERVICE.submit(() -> {
			final MapTileSelector selector = MapTileSelector.of(continentId, floorIndex, zoom, x, y);
			try {
				return DefaultMapTileService.this.mapTileCache.get(selector);
			} catch (ExecutionException e) {
				throw new Error(e);
			}
		});
	}
}
