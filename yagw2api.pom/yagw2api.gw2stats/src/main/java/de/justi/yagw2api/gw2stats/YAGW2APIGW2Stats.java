package de.justi.yagw2api.gw2stats;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-GW2Stats
 * -------------------------------------------------------------
 * Copyright (C) 2012 - 2013 Julian Stitz
 * -------------------------------------------------------------
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


import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.gw2stats.impl.GW2StatsDTOModule;
import de.justi.yagw2api.gw2stats.impl.GW2StatsServiceModule;

public enum YAGW2APIGW2Stats {
	INSTANCE;
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(YAGW2APIGW2Stats.class);

	private final Injector injector;
	private final IGW2StatsService service;

	private YAGW2APIGW2Stats() {
		this.injector = checkNotNull(Guice.createInjector(new GW2StatsDTOModule(), new GW2StatsServiceModule()));
		this.service = checkNotNull(this.injector.getInstance(IGW2StatsService.class));
	}

	public IGW2StatsService getGW2StatsService() {
		return this.service;
	}
}
