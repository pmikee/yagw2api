package de.justi.yagw2api.wrapper.impl;


import com.google.inject.AbstractModule;

import de.justi.yagw2api.wrapper.IWVW;

public class ClientModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IWVW.class).to(de.justi.yagw2api.wrapper.impl.ClientApplication.class).asEagerSingleton();
	}
}
