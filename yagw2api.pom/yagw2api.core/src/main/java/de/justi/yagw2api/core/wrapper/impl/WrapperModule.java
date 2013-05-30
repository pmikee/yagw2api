package de.justi.yagw2api.core.wrapper.impl;


import com.google.inject.AbstractModule;

import de.justi.yagw2api.core.wrapper.IWVWWrapper;

public class WrapperModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IWVWWrapper.class).to(de.justi.yagw2api.core.wrapper.impl.WVWWrapper.class).asEagerSingleton();
	}
}
