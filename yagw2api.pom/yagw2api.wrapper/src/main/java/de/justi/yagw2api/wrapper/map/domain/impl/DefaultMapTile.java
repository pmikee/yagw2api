package de.justi.yagw2api.wrapper.map.domain.impl;

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

import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagwapi.common.Files;
import de.justi.yagwapi.common.Tuple2;

final class DefaultMapTile implements MapTile {
	// STATIC
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMapTile.class);
	private static final String PLACEHOLDER_IMAGE_RESOURCE_PATH = "images/map/placeholder_256x256.gif";
	private static final Path PLACEHOLDER_256X256;
	static {
		try {
			PLACEHOLDER_256X256 = Files.getTempDir().resolve(DefaultMapTile.class.getSimpleName());
			try (final InputStream source = DefaultMapTile.class.getClassLoader().getResourceAsStream(PLACEHOLDER_IMAGE_RESOURCE_PATH)) {
				java.nio.file.Files.copy(source, PLACEHOLDER_256X256, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			LOGGER.error("Failed to initialize DefaultMapTile class", e);
			throw new IOError(e);
		}
	}

	public static final MapTileBuilder builder() {
		return new DefaultMapTileBuilder();
	}

	// EMBEDDED
	private static final class DefaultMapTileBuilder implements MapTileBuilder {

		// FIELDS
		@Nullable
		private Tuple2<Integer, Integer> position = null;
		@Nullable
		private Integer floorIndex = null;

		// CONSTRUCTOR
		private DefaultMapTileBuilder() {
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
		public MapTile build() {
			return new DefaultMapTile(this);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("position", this.position).add("floorIndex", this.floorIndex).toString();
		}
	}

	// FIELDS

	private final Tuple2<Integer, Integer> position;
	private final int floorIndex;

	// CONSTRUCTOR
	private DefaultMapTile(final DefaultMapTileBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.position = checkNotNull(builder.position, "missing position in %s", builder);
		this.floorIndex = checkNotNull(builder.floorIndex, "missing floorIndex in %s", builder);
	}

	// METHODS
	@Override
	public Path getImagePath() {
		return PLACEHOLDER_256X256;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("position", this.position).add("floorIndex", this.floorIndex).toString();
	}

}
