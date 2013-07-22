package de.justi.yagw2api.mumblelink;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.mumblelink.impl.Module;

public enum YAGW2APIMumbleLink {
	INSTANCE;

	private final Injector injector;
	private final IMumbleLink mumbleLink;

	private YAGW2APIMumbleLink() {
		this.injector = Guice.createInjector(new Module());
		checkState(this.injector != null);
		this.mumbleLink = checkNotNull(this.injector.getInstance(IMumbleLink.class));
	}

	public Injector getInjector() {
		return this.injector;
	}

	public IMumbleLink getMumbleLink() {
		return this.mumbleLink;
	}
}
