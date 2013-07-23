package de.justi.yagw2api.gw2stats;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.gw2stats.dto.impl.GW2StatsDTOModule;
import de.justi.yagw2api.gw2stats.service.IGW2StatsService;
import de.justi.yagw2api.gw2stats.service.impl.GW2StatsServiceModule;

public enum YAGW2APIGW2Stats {
	INSTANCE;
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(YAGW2APIGW2Stats.class);

	private final Injector injector;
	private final IGW2StatsService service;

	private YAGW2APIGW2Stats() {
		this.injector = checkNotNull(Guice.createInjector(new GW2StatsDTOModule(), new GW2StatsServiceModule()));
		this.service = checkNotNull(this.injector.getInstance(IGW2StatsService.class));
	}

	public IGW2StatsService getGW2StatsService() {
		return this.service;
	}
}
