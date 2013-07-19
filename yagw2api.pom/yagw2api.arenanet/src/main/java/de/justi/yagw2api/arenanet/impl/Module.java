package de.justi.yagw2api.arenanet.impl;

import java.util.Locale;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.arenanet.IGuildDTOFactory;
import de.justi.yagw2api.arenanet.IGuildService;
import de.justi.yagw2api.arenanet.IWVWDTOFactory;
import de.justi.yagw2api.arenanet.IWVWService;
import de.justi.yagw2api.arenanet.IWorldDTOFactory;
import de.justi.yagw2api.arenanet.IWorldService;

public final class Module extends AbstractModule {

	@Override
	protected void configure() {
		// factories
		this.bind(IWVWDTOFactory.class).to(WVWDTOFactory.class).asEagerSingleton();
		this.bind(IWorldDTOFactory.class).to(WorldDTOFactory.class).asEagerSingleton();
		this.bind(IGuildDTOFactory.class).to(GuildDTOFactory.class).asEagerSingleton();

		// services
		this.bind(IWVWService.class).to(WVWService.class).asEagerSingleton();
		this.bind(IGuildService.class).to(GuildService.class).asEagerSingleton();
		this.bind(IWorldService.class).to(WorldService.class).asEagerSingleton();

		// default locale
		this.bind(Locale.class).toInstance(Locale.getDefault());
	}
}
