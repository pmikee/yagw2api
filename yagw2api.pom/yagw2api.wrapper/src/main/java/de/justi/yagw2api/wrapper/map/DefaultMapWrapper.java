package de.justi.yagw2api.wrapper.map;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

import de.justi.yagw2api.arenanet.MapContinentService;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.arenanet.dto.map.MapContinentWithIdDTO;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.domain.map.Continent;
import de.justi.yagw2api.wrapper.domain.map.ContinentMap;

public enum DefaultMapWrapper implements MapWrapper {
	INSTANCE;

	// CONSTS
	private static final MapContinentService SERVICE = YAGW2APIArenanet.getInstance().getMapContinentService();
	private static final Function<MapContinentWithIdDTO, Continent> CONTINENT_CONVERTER = new Function<MapContinentWithIdDTO, Continent>() {

		@Override
		public Continent apply(final MapContinentWithIdDTO input) {
			checkNotNull(input, "missing input");
			return YAGW2APIWrapper.INSTANCE.getMapDomainFactory().newContinentBuilder().id(input.getId()).name(input.getName()).dimension(input.getDimension()).build();
		}
	};

	// EMBEDDED

	// METHODS
	@Override
	public Iterable<Continent> getContinents() {
		return Iterables.transform(SERVICE.retrieveAllContinents(), CONTINENT_CONVERTER);
	}

	@Override
	public ContinentMap getContinentMap(final Continent continent) {
		// FIXME
		return null;
	}

}
