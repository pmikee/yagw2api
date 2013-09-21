package de.justi.yagw2api.analyzer;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
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


import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.analyzer.impl.Module;
import de.justi.yagw2api.analyzer.utils.DerbyServerHelper;

public enum YAGW2APIAnalyzer {
	DEFAULT(Guice.createInjector(new Module(), new YAGWAPIAnalyzerPersistenceModule(false))), TEST(Guice.createInjector(new Module(), new YAGWAPIAnalyzerPersistenceModule(true)));

	static {
		// launch the db server
		new DerbyServerHelper().start();
	}

	private static YAGW2APIAnalyzer instance = DEFAULT;

	public static void changeYAGW2APIAnalyzerInstance(YAGW2APIAnalyzer toUse) {
		checkNotNull(toUse);
		if (toUse != YAGW2APIAnalyzer.instance) {
			YAGW2APIAnalyzer.instance = toUse;
		}
	}

	public static Injector getInjector() {
		return instance.injector;
	}

	public static IWVWAnalyzer getAnalyzer() {
		return instance.analyzer;
	}

	public static IWorldEnityDAO getWorldEntityDAO() {
		return instance.worldEntityDAO;
	}

	public static IWVWMatchEntityDAO getWVWMatchEntityDAO() {
		return instance.wvwMatchEntityDAO;
	}

	private final Injector injector;
	private final IWVWAnalyzer analyzer;
	private final IWorldEnityDAO worldEntityDAO;
	private final IWVWMatchEntityDAO wvwMatchEntityDAO;

	private YAGW2APIAnalyzer(Injector injector) {
		checkNotNull(injector);
		this.injector = injector;
		this.analyzer = this.injector.getInstance(IWVWAnalyzer.class);
		this.worldEntityDAO = this.injector.getInstance(IWorldEnityDAO.class);
		this.wvwMatchEntityDAO = this.injector.getInstance(IWVWMatchEntityDAO.class);
	}
}
