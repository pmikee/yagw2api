package de.justi.yagw2api.arenanet.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

import de.justi.yagw2api.test.AbstractYAGW2APITest;

public final class WorldNameDTOTest extends AbstractYAGW2APITest {
	@Test
	public void testGetServerLocale() {
		final WorldNameDTO dto = new WorldNameDTO();
		dto.id = 1;
		dto.name = "Baruch-Bucht [SP]";
		assertTrue(dto.getWorldLocale().isPresent());
		assertEquals(Locale.forLanguageTag("es"), dto.getWorldLocale().get());

		final WorldNameDTO dto2 = new WorldNameDTO();
		dto2.id = 2;
		dto2.name = "Roche de l'Augure [FR]";
		assertTrue(dto2.getWorldLocale().isPresent());
		assertEquals(Locale.forLanguageTag("fr"), dto2.getWorldLocale().get());

		final WorldNameDTO dto3 = new WorldNameDTO();
		dto3.id = 3;
		dto3.name = "Miller's Sound [DE]";
		assertTrue(dto3.getWorldLocale().isPresent());
		assertEquals(Locale.forLanguageTag("de"), dto3.getWorldLocale().get());

		final WorldNameDTO dto4 = new WorldNameDTO();
		dto4.id = 4;
		dto4.name = "Meer des Leids";
		assertFalse(dto4.getWorldLocale().isPresent());

		final WorldNameDTO dto5 = new WorldNameDTO();
		dto5.id = 5;
		dto5.name = "Fisura de la Aflicción";
		assertFalse(dto5.getWorldLocale().isPresent());
	}

	@Test
	public void testGetNameWithoutLocale() {
		final WorldNameDTO dto = new WorldNameDTO();
		dto.id = 1;
		dto.name = "Baruch-Bucht [SP]";
		assertEquals("Baruch-Bucht [SP]", dto.getName());
		assertEquals("Baruch-Bucht", dto.getNameWithoutLocale());

		final WorldNameDTO dto2 = new WorldNameDTO();
		dto2.id = 2;
		dto2.name = "Fisura de la Aflicción";
		assertEquals("Fisura de la Aflicción", dto2.getNameWithoutLocale());
		assertEquals(dto2.getName(), dto2.getNameWithoutLocale());

		final WorldNameDTO dto3 = new WorldNameDTO();
		dto3.id = 2;
		dto3.name = "Roche de l'Augure [FR]";
		assertEquals("Roche de l'Augure [FR]", dto3.getName());
		assertEquals("Roche de l'Augure", dto3.getNameWithoutLocale());
	}
}
