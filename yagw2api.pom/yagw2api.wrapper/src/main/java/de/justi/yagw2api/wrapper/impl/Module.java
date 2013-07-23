package de.justi.yagw2api.wrapper.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.wrapper.IModelFactory;
import de.justi.yagw2api.wrapper.IWVWModelEventFactory;
import de.justi.yagw2api.wrapper.IWVWModelFactory;
import de.justi.yagw2api.wrapper.IWVWWrapper;

final public class Module extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IModelFactory.class).to(ModelFactory.class).asEagerSingleton();
		this.bind(IWVWModelFactory.class).to(WVWModelFactory.class).asEagerSingleton();
		this.bind(IWVWModelEventFactory.class).to(WVWModelEventFactory.class).asEagerSingleton();
		this.bind(IWVWWrapper.class).to(de.justi.yagw2api.wrapper.impl.WVWWrapper.class);
	}
}