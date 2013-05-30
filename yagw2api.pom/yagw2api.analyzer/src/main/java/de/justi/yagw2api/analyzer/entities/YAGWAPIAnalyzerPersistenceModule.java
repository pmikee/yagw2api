package de.justi.yagw2api.analyzer.entities;

import com.google.inject.AbstractModule;

public class YAGWAPIAnalyzerPersistenceModule extends AbstractModule {

	private boolean testMode;
	public YAGWAPIAnalyzerPersistenceModule(boolean testMode) {
		this.testMode = testMode;
	}
	
	@Override
	protected void configure() {
		if(this.testMode) {
			// test mode
			bind(YAGW2APIAnalyzerPersistence.class).toInstance(YAGW2APIAnalyzerPersistence.TEST);
		}else {
			// default mode
			bind(YAGW2APIAnalyzerPersistence.class).toInstance(YAGW2APIAnalyzerPersistence.DEFAULT);
		}
	}

}
