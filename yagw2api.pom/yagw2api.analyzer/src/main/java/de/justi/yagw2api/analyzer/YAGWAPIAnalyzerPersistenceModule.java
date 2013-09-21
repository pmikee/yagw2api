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


import com.google.inject.AbstractModule;

public class YAGWAPIAnalyzerPersistenceModule extends AbstractModule {

	private boolean testModeEnabled;
	public YAGWAPIAnalyzerPersistenceModule(boolean testMode) {
		this.testModeEnabled = testMode;
	}
	
	@Override
	protected void configure() {
		if(this.testModeEnabled) {
			// test mode
			bind(YAGW2APIAnalyzerPersistence.class).toInstance(YAGW2APIAnalyzerPersistence.TEST);
		}else {
			// default mode
			bind(YAGW2APIAnalyzerPersistence.class).toInstance(YAGW2APIAnalyzerPersistence.DEFAULT);
		}
	}

}
