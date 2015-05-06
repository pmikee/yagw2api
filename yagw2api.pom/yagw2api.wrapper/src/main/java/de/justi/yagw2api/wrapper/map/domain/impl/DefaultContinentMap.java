package de.justi.yagw2api.wrapper.map.domain.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.MapFloorService;
import de.justi.yagw2api.wrapper.map.domain.ContinentMap;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;
import de.justi.yagw2api.wrapper.map.domain.MapFloor;
import de.justi.yagw2api.wrapper.map.domain.MapFloorTiles;

final class DefaultContinentMap implements ContinentMap {

	// STATIC
	public static ContinentMapBuilder builder(final MapDomainFactory mapDomainFactory, final MapFloorService mapFloorService) {
		return new DefaultContinentMapBuilder(checkNotNull(mapDomainFactory, "missing mapDomainFactory"), checkNotNull(mapFloorService, "missing mapFloorService"));
	}

	// EMBEDDED
	static final class DefaultContinentMapBuilder implements ContinentMap.ContinentMapBuilder {
		// FIELDS
		@Nullable
		private String continentId = null;
		private final Set<Integer> floorIds = Sets.newHashSet();
		private final MapFloorService mapFloorService;
		private final MapDomainFactory mapDomainFactory;

		// CONSTRUCTOR
		@Inject
		public DefaultContinentMapBuilder(final MapDomainFactory mapDomainFactory, final MapFloorService mapFloorService) {
			this.mapDomainFactory = checkNotNull(mapDomainFactory, "missing mapDomainFactory");
			this.mapFloorService = checkNotNull(mapFloorService, "missing mapFloorService");
		}

		// METHODS
		@Override
		public ContinentMap build() {
			return new DefaultContinentMap(this);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("continentId", this.continentId).add("floorIds", this.floorIds).toString();
		}

		@Override
		public ContinentMapBuilder continentId(final String continentId) {
			this.continentId = continentId;
			return this;
		}

		@Override
		public ContinentMapBuilder floorIds(final Set<Integer> floorIds) {
			this.floorIds.clear();
			if (floorIds != null) {
				this.floorIds.addAll(floorIds);
			}
			return this;
		}

	}

	// FIELDS
	private final MapFloorService mapFloorService;
	private final MapDomainFactory mapDomainFactory;
	private final List<MapFloor> mapFloors;

	// CONSTRUCTOR
	private DefaultContinentMap(final DefaultContinentMapBuilder builder) {
		this.mapFloorService = checkNotNull(builder.mapFloorService, "missing mapFloorService in %s", builder);
		this.mapDomainFactory = checkNotNull(builder.mapDomainFactory, "missing mapDomainFactory in %s", builder);
		this.mapFloors = FluentIterable.from(checkNotNull(builder.floorIds, "missing floorIds"))
				.<MapFloor> transform(floorId -> this.mapDomainFactory.newMapFloorBuilder().index(floorId).build()).toList();
	}

	// METHODS

	@Override
	public MapFloorTiles getFloorTiles(final MapFloor floor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MapFloor> getFloors() {
		return this.mapFloors;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("mapFloors", this.mapFloors).toString();
	}
}
