package de.justi.yagw2api.gw2stats.dto.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.gw2stats.dto.IGW2StatsDTOFactory;

public class GW2StatsDTOModule extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IGW2StatsDTOFactory.class).to(GW2StatsDTOFactory.class).asEagerSingleton();
	}

}
