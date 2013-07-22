package de.justi.yagw2api.mumblelink;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.mumblelink.impl.Module;

public enum YAGW2APIMumbleLink {
	INSTANCE;

	private final Injector injector;

	private YAGW2APIMumbleLink() {
		this.injector = Guice.createInjector(new Module());
	}

	public Injector getInjector() {
		return this.injector;
	}
}
