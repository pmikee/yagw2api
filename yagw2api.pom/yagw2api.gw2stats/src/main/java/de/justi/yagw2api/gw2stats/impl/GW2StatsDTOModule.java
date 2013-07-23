package de.justi.yagw2api.gw2stats.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.gw2stats.IGW2StatsDTOFactory;

public final class GW2StatsDTOModule extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IGW2StatsDTOFactory.class).to(GW2StatsDTOFactory.class).asEagerSingleton();
	}

}
