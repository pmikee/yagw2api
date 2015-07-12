package de.justi.yagw2api.wrapper;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
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

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

import de.justi.yagw2api.arenanet.v1.YAGW2APIArenanetV1;
import de.justi.yagw2api.wrapper.guild.DefaultGuildWrapper;
import de.justi.yagw2api.wrapper.guild.GuildWrapper;
import de.justi.yagw2api.wrapper.guild.domain.GuildDomainFactory;
import de.justi.yagw2api.wrapper.guild.domain.impl.DefaultGuildDomainFactory;
import de.justi.yagw2api.wrapper.map.DefaultMapWrapper;
import de.justi.yagw2api.wrapper.map.MapWrapper;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;
import de.justi.yagw2api.wrapper.map.domain.impl.DefaultMapDomainFactory;
import de.justi.yagw2api.wrapper.map.event.MapEventFactory;
import de.justi.yagw2api.wrapper.map.event.impl.DefaultMapEventFactory;
import de.justi.yagw2api.wrapper.world.domain.WorldDomainFactory;
import de.justi.yagw2api.wrapper.world.domain.impl.DefaultWorldDomainFactory;
import de.justi.yagw2api.wrapper.wvw.DefaultWVWWrapper;
import de.justi.yagw2api.wrapper.wvw.WVWWrapper;
import de.justi.yagw2api.wrapper.wvw.domain.WVWDomainFactory;
import de.justi.yagw2api.wrapper.wvw.domain.impl.DefaultWVWDomainFactory;
import de.justi.yagw2api.wrapper.wvw.event.WVWEventFactory;
import de.justi.yagw2api.wrapper.wvw.event.impl.DefaultWVWEventFactory;

final public class WrapperModule extends AbstractModule {
	@Override
	protected void configure() {
		this.configureArenanetModule();

		this.configureEventbus();
		this.configureLocale();
		this.configureWorld();
		this.configureGuild();
		this.configureMap();
		this.configureWVW();
	}

	private void configureArenanetModule() {
		this.install(new de.justi.yagw2api.arenanet.v1.ArenanetModule());
	}

	private void configureEventbus() {
		this.bind(EventBus.class).toInstance(new EventBus("YAGW2API-WRAPPER"));
	}

	private void configureLocale() {
		this.bind(CurrentLocaleProvider.class).toInstance(() -> YAGW2APIArenanetV1.getInstance().getCurrentLocale());
	}

	private void configureWorld() {
		this.bind(WorldDomainFactory.class).to(DefaultWorldDomainFactory.class).asEagerSingleton();
	}

	private void configureGuild() {
		this.bind(GuildDomainFactory.class).to(DefaultGuildDomainFactory.class).asEagerSingleton();
		this.bind(GuildWrapper.class).to(DefaultGuildWrapper.class).asEagerSingleton();
	}

	private void configureMap() {
		this.bind(MapDomainFactory.class).to(DefaultMapDomainFactory.class).asEagerSingleton();
		this.bind(MapEventFactory.class).to(DefaultMapEventFactory.class).asEagerSingleton();
		this.bind(MapWrapper.class).to(DefaultMapWrapper.class).asEagerSingleton();
	}

	private void configureWVW() {
		this.bind(WVWDomainFactory.class).to(DefaultWVWDomainFactory.class).asEagerSingleton();
		this.bind(WVWEventFactory.class).to(DefaultWVWEventFactory.class).asEagerSingleton();
		this.bind(WVWWrapper.class).to(DefaultWVWWrapper.class);
	}
}
