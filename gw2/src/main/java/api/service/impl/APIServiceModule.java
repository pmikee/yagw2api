package api.service.impl;

import api.service.IWVWService;

import com.google.inject.AbstractModule;

public class APIServiceModule extends AbstractModule{

	@Override
	protected void configure() {
		// services
		this.bind(IWVWService.class).to(WVWService.class).asEagerSingleton();
	}

}
