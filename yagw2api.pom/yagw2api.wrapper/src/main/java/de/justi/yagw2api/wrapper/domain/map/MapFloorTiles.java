package de.justi.yagw2api.wrapper.domain.map;

import java.util.Optional;

public interface MapFloorTiles {
	Optional<MapTile> getTileUnchecked(int x, int y);

	MapTile getTile(int x, int y) throws NoSuchMapTileException;
}
