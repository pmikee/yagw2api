package de.justi.yagw2api.analyzer.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.wvw.entities.impl.AnalyzerWVWEntitiesModule;
import de.justi.yagw2api.analyzer.wvw.impl.AnalyzerWVWModule;

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
