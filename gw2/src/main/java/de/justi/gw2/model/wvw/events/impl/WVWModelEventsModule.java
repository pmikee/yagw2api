package de.justi.gw2.model.wvw.events.impl;


import com.google.inject.AbstractModule;

import de.justi.gw2.model.wvw.events.IWVWModelEventFactory;

public class WVWModelEventsModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(IWVWModelEventFactory.class).to(WVWModelEventFactory.class).asEagerSingleton();
	}

}
