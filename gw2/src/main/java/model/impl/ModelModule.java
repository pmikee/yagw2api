package model.impl;

import model.IModelFactory;

import com.google.inject.AbstractModule;

public class ModelModule extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(IModelFactory.class).to(ModelFactory.class).asEagerSingleton();
	}
}