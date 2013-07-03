package de.justi.yagw2api.arenanet.service.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.arenanet.service.IWVWService;

public final class ArenanetServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		// services
		this.bind(IWVWService.class).to(WVWService.class).asEagerSingleton();
	}

}
