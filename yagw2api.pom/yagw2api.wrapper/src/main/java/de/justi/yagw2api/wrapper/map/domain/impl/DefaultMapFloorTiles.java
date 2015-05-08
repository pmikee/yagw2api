package de.justi.yagw2api.wrapper.map.domain.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import com.google.common.base.MoreObjects;

import de.justi.yagw2api.wrapper.map.domain.MapFloorTiles;
import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagw2api.wrapper.map.domain.NoSuchMapTileException;

final class DefaultMapFloorTiles implements MapFloorTiles {
	// CONSTS

	// STATIC
	public static final MapFloorTilesBuilder builder() {
		return new DefaultMapFloorTilesBuilder();
	}

	// EMBEDDED
	private static final class DefaultMapFloorTilesBuilder implements MapFloorTilesBuilder {
		// FIELDS

		// CONSTRUCTOR
		private DefaultMapFloorTilesBuilder() {
		}

		// METHODS
		@Override
		public MapFloorTiles build() {
			return new DefaultMapFloorTiles(this);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).toString();
		}

	}

	// FIELDS

	// CONSTRUCTOR
	private DefaultMapFloorTiles(final DefaultMapFloorTilesBuilder builder) {
		checkNotNull(builder, "missing builder");
	}

	// METHODS
	@Override
	public Optional<MapTile> getTileUnchecked(final int x, final int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapTile getTile(final int x, final int y) throws NoSuchMapTileException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).toString();
	}

}
