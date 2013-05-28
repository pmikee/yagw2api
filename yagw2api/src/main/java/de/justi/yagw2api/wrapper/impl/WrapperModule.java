package de.justi.yagw2api.wrapper.impl;


import com.google.inject.AbstractModule;

import de.justi.yagw2api.wrapper.IWVWWrapper;

public class WrapperModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IWVWWrapper.class).to(de.justi.yagw2api.wrapper.impl.WVWWrapper.class).asEagerSingleton();
	}
}
