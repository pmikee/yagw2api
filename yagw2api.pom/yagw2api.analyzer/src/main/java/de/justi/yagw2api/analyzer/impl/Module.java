package de.justi.yagw2api.analyzer.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.IWVWMatchEntityDAO;
import de.justi.yagw2api.analyzer.IWorldEnityDAO;

public final class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(IWorldEnityDAO.class).to(WorldEntityDAO.class).asEagerSingleton();
		bind(IWVWMatchEntityDAO.class).to(WVWMatchEntityDAO.class).asEagerSingleton();
		bind(IWVWAnalyzer.class).to(WVWAnalyzer.class);
	}

}
