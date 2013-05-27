package de.justi.gw2.model.impl;


import com.google.inject.AbstractModule;

import de.justi.gw2.model.IModelFactory;

public class ModelModule extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IModelFactory.class).to(ModelFactory.class).asEagerSingleton();
	}
}