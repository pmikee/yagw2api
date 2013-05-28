package de.justi.yagw2api.wrapper.model.impl;


import com.google.inject.AbstractModule;

import de.justi.yagw2api.wrapper.model.IModelFactory;

public class ModelModule extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IModelFactory.class).to(ModelFactory.class).asEagerSingleton();
	}
}