package de.justi.gw2.model.wvw.impl;


import com.google.inject.AbstractModule;

import de.justi.gw2.model.wvw.IWVWModelFactory;

public class WVWModelModule extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IWVWModelFactory.class).to(WVWModelFactory.class).asEagerSingleton();
	}
}