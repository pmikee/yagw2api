package de.justi.yagw2api.analyzer.entities.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.analyzer.entities.IWorldEnityDAO;

public final class AnalyzerEntitiesModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IWorldEnityDAO.class).to(WorldEntityDAO.class).asEagerSingleton();
	}

}
