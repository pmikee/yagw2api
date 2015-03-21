package de.justi.yagw2api.arenanet;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import java.util.Locale;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.arenanet.dto.guild.DefaultGuildDTOFactory;
import de.justi.yagw2api.arenanet.dto.guild.GuildDTOFactory;
import de.justi.yagw2api.arenanet.dto.map.DefaultMapDTOFactory;
import de.justi.yagw2api.arenanet.dto.map.MapDTOFactory;
import de.justi.yagw2api.arenanet.dto.world.DefaultWorldDTOFactory;
import de.justi.yagw2api.arenanet.dto.world.WorldDTOFactory;
import de.justi.yagw2api.arenanet.dto.wvw.DefaultWVWDTOFactory;
import de.justi.yagw2api.arenanet.dto.wvw.WVWDTOFactory;

public final class Module extends AbstractModule {

	@Override
	protected void configure() {
		// factories
		this.bind(MapDTOFactory.class).to(DefaultMapDTOFactory.class).asEagerSingleton();
		this.bind(WVWDTOFactory.class).to(DefaultWVWDTOFactory.class).asEagerSingleton();
		this.bind(WorldDTOFactory.class).to(DefaultWorldDTOFactory.class).asEagerSingleton();
		this.bind(GuildDTOFactory.class).to(DefaultGuildDTOFactory.class).asEagerSingleton();

		// services
		this.bind(WVWService.class).to(DefaultWVWService.class).asEagerSingleton();
		this.bind(GuildService.class).to(DefaultGuildService.class).asEagerSingleton();
		this.bind(WorldService.class).to(DefaultWorldService.class).asEagerSingleton();
		this.bind(MapService.class).to(DefaultMapService.class).asEagerSingleton();
		this.bind(MapTileService.class).to(DefaultMapTileService.class).asEagerSingleton();
		this.bind(MapFloorService.class).to(DefaultMapFloorService.class).asEagerSingleton();

		// default locale
		this.bind(Locale.class).toInstance(Locale.getDefault());
	}
}
