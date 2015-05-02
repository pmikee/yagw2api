package de.justi.yagw2api.wrapper.map;

import de.justi.yagw2api.arenanet.MapContinentService;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.wrapper.domain.map.Continent;
import de.justi.yagw2api.wrapper.domain.map.ContinentMap;

public enum DefaultMapWrapper implements MapWrapper {
	INSTANCE;
	private static final MapContinentService SERVICE = YAGW2APIArenanet.getInstance().getMapContinentService();

	@Override
	public Iterable<Continent> getContinents() {
		SERVICE.retrieveAllContinents(YAGW2APIArenanet.INSTANCE.getCurrentLocale());
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContinentMap getContinentMap(final Continent continent) {
		// TODO Auto-generated method stub
		return null;
	}

}
