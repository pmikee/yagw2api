package de.justi.yagw2api.core.wrapper.model.impl;


import com.google.inject.AbstractModule;

import de.justi.yagw2api.core.wrapper.model.IModelFactory;

final public class ModelModule extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IModelFactory.class).to(ModelFactory.class).asEagerSingleton();
	}
}