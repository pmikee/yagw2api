package de.justi.yagw2api.analyzer;

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
