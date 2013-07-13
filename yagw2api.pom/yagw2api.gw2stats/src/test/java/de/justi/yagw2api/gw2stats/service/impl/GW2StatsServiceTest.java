package de.justi.yagw2api.gw2stats.service.impl;

import org.junit.Test;

import de.justi.yagw2api.gw2stats.YAGW2APIGW2Stats;
import de.justi.yagw2api.gw2stats.dto.IGW2StatsDTOFactory;
import de.justi.yagw2api.test.AbstractYAGW2APITest;

public class GW2StatsServiceTest extends AbstractYAGW2APITest {
	@Test
	public void testRetrieveAPIStates() {
		GW2StatsService service = new GW2StatsService(YAGW2APIGW2Stats.getInjector().getInstance(IGW2StatsDTOFactory.class));
		service.retrieveAPIStates();
	}

	@Test
	public void testRetrieveAPIStateDescriptions() {
		GW2StatsService service = new GW2StatsService(YAGW2APIGW2Stats.getInjector().getInstance(IGW2StatsDTOFactory.class));
		service.retrieveAPIStateDescriptions();
	}
}
