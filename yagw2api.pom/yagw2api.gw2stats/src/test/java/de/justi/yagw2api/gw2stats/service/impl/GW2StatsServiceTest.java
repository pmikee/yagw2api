package de.justi.yagw2api.gw2stats.service.impl;

import org.junit.Test;

import de.justi.yagw2api.gw2stats.YAGW2APIGW2Stats;
import de.justi.yagw2api.gw2stats.dto.IGW2StatsDTOFactory;

public class GW2StatsServiceTest {
	@Test
	public void testRetrieveStatus() {
		GW2StatsService service = new GW2StatsService(YAGW2APIGW2Stats.getInjector().getInstance(IGW2StatsDTOFactory.class));
		service.retrieveAPIStatus();
	}
}
