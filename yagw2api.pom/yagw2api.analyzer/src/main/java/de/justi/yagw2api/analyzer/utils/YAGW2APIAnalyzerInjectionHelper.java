package de.justi.yagw2api.analyzer.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.analyzer.wvw.impl.AnalyzerWVWModule;

public enum YAGW2APIAnalyzerInjectionHelper {
	INSTANCE;

	public static Injector getInjector() {
		return INSTANCE.injector;
	}

	private final Injector injector = Guice.createInjector(new AnalyzerWVWModule());
}
