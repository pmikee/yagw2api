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

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagwapi.common.io.Files;
import de.justi.yagwapi.common.tuple.Tuple2;

class DefaultUnavailableMapTile implements MapTile {

	// STATIC
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMapTile.class);
	private static final String PLACEHOLDER_IMAGE_RESOURCE_PATH = "images/map/placeholder_256x256.gif";
	protected static final Path PLACEHOLDER_256X256;
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

	public static MapTile.MapTileBuilder builder() {
		return new DefaultUnavailableMapTileBuilder();
	}

	// EMBEDDED
	protected abstract static class AbstractMapTileBuilder<B extends AbstractMapTileBuilder<B>> implements MapTileBuilder {

		// FIELDS
		@Nullable
		private Tuple2<Integer, Integer> position = null;
		@Nullable
		private Integer floorIndex = null;
		@Nullable
		private Integer zoom = null;
		@Nullable
		private String continentId = null;

		// CONSTRUCTOR
		protected AbstractMapTileBuilder() {
		}

		// METHODS
		protected abstract B self();

		@Override
		public abstract MapTile build();

		@Override
		public final B position(@Nullable final Tuple2<Integer, Integer> position) {
			this.position = position;
			return this.self();
		}

		@Override
		public final B floorIndex(final int floorIndex) {
			this.floorIndex = floorIndex;
			return this.self();
		}

		@Override
		public final B continentId(@Nullable final String continentId) {
			this.continentId = continentId;
			return this.self();
		}

		@Override
		public final B zoom(final int zoom) {
			this.zoom = zoom;
			return this.self();
		}

		/**
		 * @return the position
		 */
		protected final Tuple2<Integer, Integer> getPosition() {
			return this.position;
		}

		/**
		 * @return the floorIndex
		 */
		protected final Integer getFloorIndex() {
			return this.floorIndex;
		}

		/**
		 * @return the zoom
		 */
		protected final Integer getZoom() {
			return this.zoom;
		}

		/**
		 * @return the continentId
		 */
		protected final String getContinentId() {
			return this.continentId;
		}

		protected ToStringHelper toStringHelper() {
			return MoreObjects.toStringHelper(this).add("position", this.position).add("floorIndex", this.floorIndex).add("zoom", this.zoom).add("continentId", this.continentId);
		}

		@Override
		public final String toString() {
			return this.toStringHelper().toString();
		}
	}

	private static final class DefaultUnavailableMapTileBuilder extends AbstractMapTileBuilder<DefaultUnavailableMapTileBuilder> {

		@Override
		protected DefaultUnavailableMapTileBuilder self() {
			return this;
		}

		@Override
		public MapTile build() {
			return new DefaultUnavailableMapTile(this);
		}

	}

	// FIELDS
	private final Tuple2<Integer, Integer> position;
	private final int floorIndex;
	private final int zoom;
	private final String continentId;

	// CONSTRUCTOR
	public DefaultUnavailableMapTile(final AbstractMapTileBuilder<?> builder) {
		checkNotNull(builder, "missing builder");
		this.position = checkNotNull(builder.getPosition(), "missing position in %s", builder);
		checkNotNull(this.position.v1(), "incomplete position=%s of %s", this.position, this);
		checkNotNull(this.position.v2(), "incomplete position=%s of %s", this.position, this);
		this.floorIndex = checkNotNull(builder.getFloorIndex(), "missing floorIndex in %s", builder);
		this.zoom = checkNotNull(builder.getZoom(), "missing zoom in %s", builder);
		this.continentId = checkNotNull(builder.getContinentId(), "missing continentId in %s", builder);
	}

	// METHODS
	@Override
	public final Tuple2<Integer, Integer> getPosition() {
		return this.position;
	}

	@Override
	public final int getFloorIndex() {
		return this.floorIndex;
	}

	@Override
	public final int getZoom() {
		return this.zoom;
	}

	@Override
	public final String getContinentId() {
		return this.continentId;
	}

	@Override
	public final String toString() {
		return this.toStringHelper().toString();
	}

	protected ToStringHelper toStringHelper() {
		return MoreObjects.toStringHelper(this).add("position", this.position).add("floorIndex", this.floorIndex).add("zoom", this.zoom).add("continentId", this.continentId);
	}

	@Override
	public Path getImagePath() {
		return PLACEHOLDER_256X256;
	}

}