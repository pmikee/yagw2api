package de.justi.yagw2api.core.arenanet.service.impl;


import com.google.inject.AbstractModule;

import de.justi.yagw2api.core.arenanet.service.IWVWService;

public class APIServiceModule extends AbstractModule{

	@Override
	protected void configure() {
		// services
		this.bind(IWVWService.class).to(WVWService.class).asEagerSingleton();
	}

}
