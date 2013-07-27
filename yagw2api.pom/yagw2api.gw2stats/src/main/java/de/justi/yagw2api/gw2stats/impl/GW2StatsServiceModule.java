package de.justi.yagw2api.gw2stats.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.gw2stats.IGW2StatsService;

public final class GW2StatsServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		// services
		this.bind(IGW2StatsService.class).to(GW2StatsService.class).asEagerSingleton();
	}

}