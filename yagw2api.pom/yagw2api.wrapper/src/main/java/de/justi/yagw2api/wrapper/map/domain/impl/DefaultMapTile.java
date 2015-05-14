package de.justi.yagw2api.wrapper.map.domain.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
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
import static com.google.common.base.Preconditions.checkState;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import de.justi.yagw2api.arenanet.MapTileService;
import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagw2api.wrapper.map.event.MapEventFactory;
import de.justi.yagwapi.common.Files;
import de.justi.yagwapi.common.Tuple2;

final class DefaultMapTile implements MapTile, FutureCallback<Optional<Path>> {
	// STATIC
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMapTile.class);
	private static final String PLACEHOLDER_IMAGE_RESOURCE_PATH = "images/map/placeholder_256x256.gif";
	private static final Path PLACEHOLDER_256X256;
	static {
		try {
			final Path tempDir = Files.getTempDir().resolve("yagw2api").resolve(DefaultMapTile.class.getSimpleName());
			java.nio.file.Files.createDirectories(tempDir);
			PLACEHOLDER_256X256 = tempDir.resolve("placeholder_256x256.gif");
			try (final InputStream source = DefaultMapTile.class.getClassLoader().getResourceAsStream(PLACEHOLDER_IMAGE_RESOURCE_PATH)) {
				java.nio.file.Files.copy(source, PLACEHOLDER_256X256, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			LOGGER.error("Failed to initialize DefaultMapTile class", e);
			throw new IOError(e);
		}
	}

	public static final MapTileBuilder builder(final EventBus eventbus, final MapTileService mapTileService, final MapEventFactory mapEventFactory) {
		return new DefaultMapTileBuilder(checkNotNull(eventbus, "missing eventbus"), checkNotNull(mapTileService, "missing mapTileService"), checkNotNull(mapEventFactory,
				"missing mapEventFactory"));
	}

	// EMBEDDED
	private static final class DefaultMapTileBuilder implements MapTileBuilder {

		// FIELDS
		private final EventBus eventbus;
		private final MapTileService mapTileService;
		private final MapEventFactory mapEventFactory;
		@Nullable
		private Tuple2<Integer, Integer> position = null;
		@Nullable
		private Integer floorIndex = null;
		@Nullable
		private Integer zoom = null;
		@Nullable
		private String continentId = null;

		// CONSTRUCTOR
		private DefaultMapTileBuilder(final EventBus eventbus, final MapTileService mapTileService, final MapEventFactory mapEventFactory) {
			this.eventbus = checkNotNull(eventbus, "missing eventbus");
			this.mapTileService = checkNotNull(mapTileService, "missing mapTileService");
			this.mapEventFactory = checkNotNull(mapEventFactory, "missing mapEventFactory");
		}

		// METHODS
		@Override
		public MapTileBuilder position(@Nullable final Tuple2<Integer, Integer> position) {
			this.position = position;
			return this;
		}

		@Override
		public MapTileBuilder floorIndex(final int floorIndex) {
			this.floorIndex = floorIndex;
			return this;
		}

		@Override
		public MapTileBuilder continentId(@Nullable final String continentId) {
			this.continentId = continentId;
			return this;
		}

		@Override
		public MapTileBuilder zoom(final int zoom) {
			this.zoom = zoom;
			return this;
		}

		@Override
		public MapTile build() {
			return new DefaultMapTile(this);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("position", this.position).add("floorIndex", this.floorIndex).add("zoom", this.zoom).add("continentId", this.continentId)
					.toString();
		}
	}

	// FIELDS

	private final EventBus eventbus;
	private final MapTileService mapTileService;
	private final MapEventFactory mapEventFactory;
	private final Tuple2<Integer, Integer> position;
	private final int floorIndex;
	private final int zoom;
	private final String continentId;
	private final AtomicBoolean loading = new AtomicBoolean(false);
	private volatile Optional<Path> path = null;

	// CONSTRUCTOR
	private DefaultMapTile(final DefaultMapTileBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.eventbus = checkNotNull(builder.eventbus, "missing eventbus in %s", builder);
		this.mapTileService = checkNotNull(builder.mapTileService, "missing mapTileService in %s", builder);
		this.mapEventFactory = checkNotNull(builder.mapEventFactory, "missing mapEventFactory in %s", builder);
		this.position = checkNotNull(builder.position, "missing position in %s", builder);
		checkNotNull(this.position.v1(), "incomplete position=%s of %s", this.position, this);
		checkNotNull(this.position.v2(), "incomplete position=%s of %s", this.position, this);
		this.floorIndex = checkNotNull(builder.floorIndex, "missing floorIndex in %s", builder);
		this.zoom = checkNotNull(builder.zoom, "missing zoom in %s", builder);
		this.continentId = checkNotNull(builder.continentId, "missing continentId in %s", builder);
	}

	// METHODS
	@Override
	public Path getImagePath() {
		if (!this.loading.getAndSet(true)) {
			final ListenableFuture<Optional<Path>> tilePathFuture = this.mapTileService.getMapTileAsync(this.continentId, this.floorIndex, this.zoom, this.position.v1(),
					this.position.v2());
			try {
				this.path = tilePathFuture.get(0, TimeUnit.MILLISECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				this.path = null;
				Futures.addCallback(tilePathFuture, this);
			}
		}
		return this.path != null ? this.path.or(PLACEHOLDER_256X256) : PLACEHOLDER_256X256;
	}

	@Override
	public void onSuccess(final Optional<Path> result) {
		checkNotNull(result, "missing result");
		checkState(this.path == null, "path is already set: %s", this.path);
		this.path = result;
		if (this.path.isPresent()) {
			this.eventbus.post(this.mapEventFactory.newMapTileImageLoadedSuccessfully(this));
		} else {
			this.eventbus.post(this.mapEventFactory.newMapTileImageNotAvailable(this));
		}
	}

	@Override
	public void onFailure(final Throwable t) {
		checkNotNull(t, "missing throwable");
		checkState(this.path == null, "path is already set: %s", this.path);
		this.path = Optional.absent();
		this.eventbus.post(this.mapEventFactory.newMapTileImageFailedToLoad(this));
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("position", this.position).add("floorIndex", this.floorIndex).add("zoom", this.zoom).add("continentId", this.continentId)
				.toString();
	}

}
