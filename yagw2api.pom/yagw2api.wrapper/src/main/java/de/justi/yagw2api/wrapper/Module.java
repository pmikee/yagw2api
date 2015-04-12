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

import de.justi.yagw2api.wrapper.domain.IModelFactory;
import de.justi.yagw2api.wrapper.domain.ModelFactory;
import de.justi.yagw2api.wrapper.domain.wvw.IWVWModelFactory;
import de.justi.yagw2api.wrapper.domain.wvw.IWVWWrapper;
import de.justi.yagw2api.wrapper.domain.wvw.WVWModelFactory;
import de.justi.yagw2api.wrapper.domain.wvw.event.IWVWModelEventFactory;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWModelEventFactory;
import de.justi.yagw2api.wrapper.wvw.WVWWrapper;

final public class Module extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IModelFactory.class).to(ModelFactory.class).asEagerSingleton();
		this.bind(IWVWModelFactory.class).to(WVWModelFactory.class).asEagerSingleton();
		this.bind(IWVWModelEventFactory.class).to(WVWModelEventFactory.class).asEagerSingleton();
		this.bind(IWVWWrapper.class).to(WVWWrapper.class);
	}
}
