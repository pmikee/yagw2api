package de.justi.yagw2api.arenanet.dto.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.arenanet.dto.IWVWDTOFactory;

public final class ArenanetDTOModule extends AbstractModule {

	@Override
	protected void configure() {
		// factories
		this.bind(IWVWDTOFactory.class).to(WVWDTOFactory.class).asEagerSingleton();
	}

}
