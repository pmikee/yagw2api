package de.justi.yagw2api.core.wrapper.model.wvw.impl;


import com.google.inject.AbstractModule;

import de.justi.yagw2api.core.wrapper.model.wvw.IWVWModelFactory;

public class WVWModelModule extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IWVWModelFactory.class).to(WVWModelFactory.class).asEagerSingleton();
	}
}