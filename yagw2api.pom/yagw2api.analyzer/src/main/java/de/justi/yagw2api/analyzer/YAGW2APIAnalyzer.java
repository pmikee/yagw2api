package de.justi.yagw2api.analyzer;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.analyzer.entities.wvw.impl.AnalyzerWVWEntitiesModule;
import de.justi.yagw2api.analyzer.impl.AnalyzerWVWModule;
import de.justi.yagw2api.analyzer.wvw.IWVWAnalyzer;

public enum YAGW2APIAnalyzer {
	INSTANCE;

	public static Injector getInjector() {
		return INSTANCE.injector;
	}
	
	public static IWVWAnalyzer getAnalyzer() {
		return INSTANCE.analyzer;
	}

	private final Injector injector = Guice.createInjector(new AnalyzerWVWModule(), new AnalyzerWVWEntitiesModule());
	private final IWVWAnalyzer analyzer = this.injector.getInstance(IWVWAnalyzer.class);
}
