package client.impl;

import client.IClientApplication;

import com.google.inject.AbstractModule;

public class ClientModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IClientApplication.class).to(client.impl.ClientApplication.class).asEagerSingleton();
	}
}
