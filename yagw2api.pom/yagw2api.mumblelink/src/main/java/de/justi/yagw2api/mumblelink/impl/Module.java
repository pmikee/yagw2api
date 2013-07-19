package de.justi.yagw2api.mumblelink.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.mumblelink.IMumbleLink;

public final class Module extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(IMumbleLink.class).to(MumbleLink.class).asEagerSingleton();
	}
}
