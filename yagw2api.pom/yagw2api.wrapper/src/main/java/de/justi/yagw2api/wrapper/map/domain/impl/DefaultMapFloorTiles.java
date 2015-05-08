package de.justi.yagw2api.wrapper.map.domain.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.MapTileService;
import de.justi.yagw2api.wrapper.map.domain.MapFloorTiles;
import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagw2api.wrapper.map.domain.NoSuchMapTileException;

final class DefaultMapFloorTiles implements MapFloorTiles {
	// CONSTS

	// STATIC
	public static final MapFloorTilesBuilder builder(final MapTileService mapTileService) {
		return new DefaultMapFloorTilesBuilder(checkNotNull(mapTileService, "missing mapTileService"));
	}

	// EMBEDDED
	private static final class DefaultMapFloorTilesBuilder implements MapFloorTilesBuilder {
		// FIELDS
		private final MapTileService mapTileService;
		@Nullable
		private Integer floorIndex = null;
		@Nullable
		private String continentId = null;

		// CONSTRUCTOR
		@Inject
		private DefaultMapFloorTilesBuilder(final MapTileService mapTileService) {
			this.mapTileService = checkNotNull(mapTileService, "missing mapTileService");
		}

		// METHODS
		@Override
		public MapFloorTilesBuilder continentId(final String continentId) {
			this.continentId = continentId;
			return this;
		}

		@Override
		public MapFloorTilesBuilder floorIndex(final int floorIndex) {
			this.floorIndex = floorIndex;
			return this;
		}

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
	private final MapTileService mapTileService;
	private final int floorIndex;
	private final String continentId;

	// CONSTRUCTOR
	private DefaultMapFloorTiles(final DefaultMapFloorTilesBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.mapTileService = checkNotNull(builder.mapTileService, "missing mapTileService in %s", builder);
		this.floorIndex = checkNotNull(builder.floorIndex, "missing floorIndex in %s", builder);
		this.continentId = checkNotNull(builder.continentId, "missing continentId in %s", builder);
	}

	// METHODS
	@Override
	public Optional<MapTile> getTileUnchecked(final int x, final int y, final int zoom) {
		try {
			return Optional.of(this.getTile(x, y, zoom));
		} catch (NoSuchMapTileException e) {
			return Optional.empty();
		}
	}

	@Override
	public MapTile getTile(final int x, final int y, final int zoom) throws NoSuchMapTileException {
		this.mapTileService.getMapTile(this.continentId, this.floorIndex, zoom, x, y);
		return new MapTile() {
		};
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("continentId", this.continentId).add("floorIndex", this.floorIndex).toString();
	}

}
