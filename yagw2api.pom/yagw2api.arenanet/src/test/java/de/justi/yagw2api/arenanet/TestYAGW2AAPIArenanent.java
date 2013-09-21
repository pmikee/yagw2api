package de.justi.yagw2api.arenanet;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


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
