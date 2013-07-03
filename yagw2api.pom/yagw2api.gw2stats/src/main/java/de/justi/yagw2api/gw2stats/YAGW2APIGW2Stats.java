package de.justi.yagw2api.gw2stats;

import static com.google.common.base.Preconditions.checkState;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.gw2stats.dto.impl.GW2StatsDTOModule;
import de.justi.yagw2api.gw2stats.service.impl.GW2StatsServiceModule;

public enum YAGW2APIGW2Stats {
	INSTANCE;
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(YAGW2APIGW2Stats.class);

	public static Injector getInjector() {
		checkState(INSTANCE != null);
		checkState(INSTANCE.injector != null);
		return INSTANCE.injector;
	}

	private final Injector injector;

	private YAGW2APIGW2Stats() {
		this.injector = Guice.createInjector(new GW2StatsDTOModule(), new GW2StatsServiceModule());
	}
}
