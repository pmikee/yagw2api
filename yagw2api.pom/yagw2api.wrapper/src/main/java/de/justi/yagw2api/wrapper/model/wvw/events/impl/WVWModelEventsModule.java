package de.justi.yagw2api.wrapper.model.wvw.events.impl;


import com.google.inject.AbstractModule;

import de.justi.yagw2api.wrapper.model.wvw.events.IWVWModelEventFactory;

public final class WVWModelEventsModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(IWVWModelEventFactory.class).to(WVWModelEventFactory.class).asEagerSingleton();
	}

}
