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

import com.google.inject.AbstractModule;

import de.justi.yagw2api.wrapper.domain.DefaultDomainFactory;
import de.justi.yagw2api.wrapper.domain.DomainFactory;
import de.justi.yagw2api.wrapper.domain.map.DefaultMapDomainFactory;
import de.justi.yagw2api.wrapper.domain.map.MapDomainFactory;
import de.justi.yagw2api.wrapper.domain.wvw.DefaultWVWDomainFactory;
import de.justi.yagw2api.wrapper.domain.wvw.WVWDomainFactory;
import de.justi.yagw2api.wrapper.domain.wvw.event.DefaultWVWModelEventFactory;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWModelEventFactory;
import de.justi.yagw2api.wrapper.wvw.DefaultWVWWrapper;
import de.justi.yagw2api.wrapper.wvw.WVWWrapper;

final public class Module extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(DomainFactory.class).to(DefaultDomainFactory.class).asEagerSingleton();
		this.bind(WVWDomainFactory.class).toInstance(DefaultWVWDomainFactory.INSTANCE);
		this.bind(MapDomainFactory.class).toInstance(DefaultMapDomainFactory.INSTANCE);
		this.bind(WVWModelEventFactory.class).to(DefaultWVWModelEventFactory.class).asEagerSingleton();
		this.bind(WVWWrapper.class).to(DefaultWVWWrapper.class);
	}
}
