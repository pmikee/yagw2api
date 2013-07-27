package de.justi.yagw2api.anchorman;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.anchorman.impl.Module;

public enum YAGW2APIAnchorman {
	INSTANCE;

	private final IAnchorman anchorman;

	private YAGW2APIAnchorman() {
		final Injector injector = Guice.createInjector(new Module());
		this.anchorman = injector.getInstance(IAnchorman.class);
	}

	public IAnchorman getAnchorman() {
		return this.anchorman;
	}
}
