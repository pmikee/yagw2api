package de.justi.yagw2api.wrapper.map;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import de.justi.yagw2api.arenanet.MapService;
import de.justi.yagw2api.arenanet.dto.map.MapContinentWithIdDTO;
import de.justi.yagw2api.wrapper.map.domain.Continent;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;

final class MapContinentWithIdDTO2ContinentFunction implements Function<MapContinentWithIdDTO, Continent> {
	// FIELDS
	private final MapService mapService;
	private final MapDomainFactory mapDomainFactory;

	// CONSTRUCTOR
	public MapContinentWithIdDTO2ContinentFunction(final MapService mapService, final MapDomainFactory mapDomainFactory) {
		this.mapService = checkNotNull(mapService, "missing mapService");
		this.mapDomainFactory = checkNotNull(mapDomainFactory, "missing mapDomainFactory");
	}

	// METHODS
	@Override
	public Continent apply(@Nullable final MapContinentWithIdDTO c) {
		checkNotNull(c, "missing input");
		final Set<String> mapIds = FluentIterable.from(this.mapService.retrieveAllMaps().get().getMaps().entrySet())
				.filter((mapEntry) -> mapEntry.getValue().getContinentId().equals(c.getId())).transform((mapEntry) -> mapEntry.getKey()).toSet();
		//@formatter:off
		return this.mapDomainFactory.newContinentBuilder().
					id(c.getId()).
					name(c.getName()).
					mapIds(mapIds).
					floorIds(c.getFloors()).
					dimension(c.getDimension()).
					minZoom(c.getMinZoom()).
					maxZoom(c.getMaxZoom()).
				build();
		//@formatter:on
	}
}