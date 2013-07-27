package de.justi.yagw2api.anchorman.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.anchorman.IAnchorman;

public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(IAnchorman.class).to(Anchorman.class);
	}

}
