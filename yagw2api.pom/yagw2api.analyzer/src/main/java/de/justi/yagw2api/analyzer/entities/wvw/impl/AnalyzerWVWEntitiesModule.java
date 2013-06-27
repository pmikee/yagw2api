package de.justi.yagw2api.analyzer.entities.wvw.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntityDAO;

public final class AnalyzerWVWEntitiesModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IWVWMatchEntityDAO.class).to(WVWMatchEntityDAO.class).asEagerSingleton();
	}

}
