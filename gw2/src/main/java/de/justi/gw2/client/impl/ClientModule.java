package de.justi.gw2.client.impl;


import com.google.inject.AbstractModule;

import de.justi.gw2.client.IClientApplication;

public class ClientModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IClientApplication.class).to(de.justi.gw2.client.impl.ClientApplication.class).asEagerSingleton();
	}
}
