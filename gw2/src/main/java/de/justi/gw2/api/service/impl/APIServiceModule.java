package de.justi.gw2.api.service.impl;


import com.google.inject.AbstractModule;

import de.justi.gw2.api.service.IWVWService;

public class APIServiceModule extends AbstractModule{

	@Override
	protected void configure() {
		// services
		this.bind(IWVWService.class).to(WVWService.class).asEagerSingleton();
	}

}
