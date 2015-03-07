package de.justi.yagw2api.arenanet.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */

import java.util.Locale;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.arenanet.IGuildDTOFactory;
import de.justi.yagw2api.arenanet.IGuildService;
import de.justi.yagw2api.arenanet.IMapDTOFactory;
import de.justi.yagw2api.arenanet.IMapFloorService;
import de.justi.yagw2api.arenanet.IMapTileService;
import de.justi.yagw2api.arenanet.IWVWDTOFactory;
import de.justi.yagw2api.arenanet.IWVWService;
import de.justi.yagw2api.arenanet.IWorldDTOFactory;
import de.justi.yagw2api.arenanet.IWorldService;

public final class Module extends AbstractModule {

	@Override
	protected void configure() {
		// factories
		this.bind(IMapDTOFactory.class).to(MapDTOFactory.class).asEagerSingleton();
		this.bind(IWVWDTOFactory.class).to(WVWDTOFactory.class).asEagerSingleton();
		this.bind(IWorldDTOFactory.class).to(WorldDTOFactory.class).asEagerSingleton();
		this.bind(IGuildDTOFactory.class).to(GuildDTOFactory.class).asEagerSingleton();

		// services
		this.bind(IWVWService.class).to(WVWService.class).asEagerSingleton();
		this.bind(IGuildService.class).to(GuildService.class).asEagerSingleton();
		this.bind(IWorldService.class).to(WorldService.class).asEagerSingleton();
		this.bind(IMapTileService.class).to(MapTileService.class).asEagerSingleton();
		this.bind(IMapFloorService.class).to(MapFloorService.class).asEagerSingleton();

		// default locale
		this.bind(Locale.class).toInstance(Locale.getDefault());
	}
}
