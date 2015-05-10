package de.justi.yagw2api.wrapper.map.domain.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.file.Path;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;

import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagwapi.common.Tuple2;

final class DefaultMapTile implements MapTile {
	// STATIC
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("position", this.position).add("floorIndex", this.floorIndex).toString();
	}

}
