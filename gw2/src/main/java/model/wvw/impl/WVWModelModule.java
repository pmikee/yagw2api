package model.wvw.impl;

import model.wvw.IWVWModelFactory;

import com.google.inject.AbstractModule;

public class WVWModelModule extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IWVWModelFactory.class).to(WVWModelFactory.class).asEagerSingleton();
	}
}