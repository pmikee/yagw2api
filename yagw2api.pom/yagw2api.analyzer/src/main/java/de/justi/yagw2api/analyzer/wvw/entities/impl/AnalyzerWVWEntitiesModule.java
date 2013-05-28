package de.justi.yagw2api.analyzer.wvw.entities.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.analyzer.wvw.IWVWWorldEnityDAO;

public class AnalyzerWVWEntitiesModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IWVWWorldEnityDAO.class).to(WVWWorldEntityDAO.class).asEagerSingleton();
	}

}
