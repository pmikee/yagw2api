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

import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import de.justi.yagw2api.arenanet.MapTileService;
import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagw2api.wrapper.map.event.MapEventFactory;

final class DefaultMapTile extends DefaultUnavailableMapTile implements MapTile, FutureCallback<Optional<Path>> {

	public static final MapTileBuilder builder(final EventBus eventbus, final MapTileService mapTileService, final MapEventFactory mapEventFactory) {
		return new DefaultMapTileBuilder(checkNotNull(eventbus, "missing eventbus"), checkNotNull(mapTileService, "missing mapTileService"), checkNotNull(mapEventFactory,
				"missing mapEventFactory"));
	}

	// EMBEDDED
	private static final class DefaultMapTileBuilder extends AbstractMapTileBuilder<DefaultMapTileBuilder> implements MapTileBuilder {

		// FIELDS
		private final EventBus eventbus;
		private final MapTileService mapTileService;
		private final MapEventFactory mapEventFactory;

		// CONSTRUCTOR
		private DefaultMapTileBuilder(final EventBus eventbus, final MapTileService mapTileService, final MapEventFactory mapEventFactory) {
			this.eventbus = checkNotNull(eventbus, "missing eventbus");
			this.mapTileService = checkNotNull(mapTileService, "missing mapTileService");
			this.mapEventFactory = checkNotNull(mapEventFactory, "missing mapEventFactory");
		}

		// METHODS

		@Override
		public MapTile build() {
			return new DefaultMapTile(this);
		}

		@Override
		protected DefaultMapTileBuilder self() {
			return this;
		}
	}

	// FIELDS

	private final EventBus eventbus;
	private final MapTileService mapTileService;
	private final MapEventFactory mapEventFactory;
	private final AtomicBoolean loading = new AtomicBoolean(false);
	private volatile Optional<Path> path = null;

	// CONSTRUCTOR
	private DefaultMapTile(final DefaultMapTileBuilder builder) {
		super(checkNotNull(builder, "missing builder"));
		this.eventbus = checkNotNull(builder.eventbus, "missing eventbus in %s", builder);
		this.mapTileService = checkNotNull(builder.mapTileService, "missing mapTileService in %s", builder);
		this.mapEventFactory = checkNotNull(builder.mapEventFactory, "missing mapEventFactory in %s", builder);
	}

	// METHODS
	@Override
	public Path getImagePath() {
		if (!this.loading.getAndSet(true)) {
			final ListenableFuture<Optional<Path>> tilePathFuture = this.mapTileService.getMapTileAsync(this.getContinentId(), this.getFloorIndex(), this.getZoom(), this
					.getPosition().v1(), this.getPosition().v2());
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
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("path", this.path);
	}
}
