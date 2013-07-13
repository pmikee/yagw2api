package de.justi.yagw2api.arenanet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;

import de.justi.yagw2api.test.AbstractYAGW2APITest;

public final class TestYAGW2AAPIArenanent extends AbstractYAGW2APITest {

	@Test
	public void testGetCurrentLocale() {
		assertNotNull(YAGW2APIArenanet.getInstance().getCurrentLocale());

		YAGW2APIArenanet.getInstance().setCurrentLocale(Locale.ITALIAN);
		assertNotNull(YAGW2APIArenanet.getInstance().getCurrentLocale());
		assertEquals(Locale.ITALIAN, YAGW2APIArenanet.getInstance().getCurrentLocale());

		YAGW2APIArenanet.getInstance().setCurrentLocale(Locale.TAIWAN);
		assertNotNull(YAGW2APIArenanet.getInstance().getCurrentLocale());
		assertEquals(Locale.TAIWAN, YAGW2APIArenanet.getInstance().getCurrentLocale());
	}

	@Test
	public void testIfServicesesHaveBeenInjected() {
		assertNotNull(YAGW2APIArenanet.getInstance().getWVWService());
		assertNotNull(YAGW2APIArenanet.getInstance().getWorldService());
		assertNotNull(YAGW2APIArenanet.getInstance().getGuildService());
	}
}
