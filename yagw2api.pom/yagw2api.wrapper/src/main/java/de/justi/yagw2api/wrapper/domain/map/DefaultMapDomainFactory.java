package de.justi.yagw2api.wrapper.domain.map;

import de.justi.yagw2api.wrapper.domain.map.Continent.ContinentBuilder;

public enum DefaultMapDomainFactory implements MapDomainFactory {
	INSTANCE;

	@Override
	public ContinentBuilder newContinentBuilder() {
		return DefaultContinent.builder();
	}

}
