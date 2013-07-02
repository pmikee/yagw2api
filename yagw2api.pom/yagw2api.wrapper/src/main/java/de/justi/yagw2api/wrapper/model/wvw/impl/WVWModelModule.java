package de.justi.yagw2api.wrapper.model.wvw.impl;


import com.google.inject.AbstractModule;

import de.justi.yagw2api.wrapper.model.wvw.IWVWModelFactory;

public final class WVWModelModule extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IWVWModelFactory.class).to(WVWModelFactory.class).asEagerSingleton();
	}
}