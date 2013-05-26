package model.wvw.events.impl;

import model.wvw.events.IWVWModelEventFactory;

import com.google.inject.AbstractModule;

public class WVWModelEventsModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(IWVWModelEventFactory.class).to(WVWModelEventFactory.class).asEagerSingleton();
	}

}
