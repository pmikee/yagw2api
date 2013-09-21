package de.justi.yagw2api.wrapper.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
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


import com.google.inject.AbstractModule;

import de.justi.yagw2api.wrapper.IModelFactory;
import de.justi.yagw2api.wrapper.IWVWModelEventFactory;
import de.justi.yagw2api.wrapper.IWVWModelFactory;
import de.justi.yagw2api.wrapper.IWVWWrapper;

final public class Module extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IModelFactory.class).to(ModelFactory.class).asEagerSingleton();
		this.bind(IWVWModelFactory.class).to(WVWModelFactory.class).asEagerSingleton();
		this.bind(IWVWModelEventFactory.class).to(WVWModelEventFactory.class).asEagerSingleton();
		this.bind(IWVWWrapper.class).to(de.justi.yagw2api.wrapper.impl.WVWWrapper.class);
	}
}
