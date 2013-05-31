package de.justi.yagw2api.analyzer;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.analyzer.entities.YAGWAPIAnalyzerPersistenceModule;
import de.justi.yagw2api.analyzer.entities.impl.AnalyzerEntitiesModule;
import de.justi.yagw2api.analyzer.entities.wvw.impl.AnalyzerWVWEntitiesModule;
import de.justi.yagw2api.analyzer.impl.AnalyzerWVWModule;

public enum YAGW2APIAnalyzer {
	DEFAULT(Guice.createInjector(new AnalyzerWVWModule(),new AnalyzerEntitiesModule(), new AnalyzerWVWEntitiesModule(), new YAGWAPIAnalyzerPersistenceModule(false))), TEST(Guice.createInjector(
			new AnalyzerWVWModule(), new AnalyzerEntitiesModule(), new AnalyzerWVWEntitiesModule(), new YAGWAPIAnalyzerPersistenceModule(true)));

	private static YAGW2APIAnalyzer instance = DEFAULT;

	public static void changeYAGW2APIAnalyzerInstance(YAGW2APIAnalyzer toUse) {
		checkNotNull(toUse);
		if(toUse != YAGW2APIAnalyzer.instance) {
			YAGW2APIAnalyzer.instance = toUse;
		}
	}

	public static Injector getInjector() {
		return instance.injector;
	}

	public static IWVWAnalyzer getAnalyzer() {
		return instance.analyzer;
	}

	private final Injector injector;
	private final IWVWAnalyzer analyzer;

	private YAGW2APIAnalyzer(Injector injector) {
		checkNotNull(injector);
		this.injector = injector;
		this.analyzer = this.injector.getInstance(IWVWAnalyzer.class);
	}
}
