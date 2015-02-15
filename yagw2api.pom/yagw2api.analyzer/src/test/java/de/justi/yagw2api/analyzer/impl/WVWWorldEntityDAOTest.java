package de.justi.yagw2api.analyzer.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.justi.yagw2api.analyzer.AbstractAnalyzerTest;
import de.justi.yagw2api.analyzer.impl.WorldEntityDAO;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.wrapper.IModelFactory;
import de.justi.yagw2api.wrapper.IWorld;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.impl.WorldLocationType;

public final class WVWWorldEntityDAOTest extends AbstractAnalyzerTest {

	private IModelFactory modelFactory = YAGW2APIWrapper.INSTANCE.getModelFactory();

	private IWorld world1;
	private IWorld world2;
	private IWorld world3;
	private IWorld world4;

	@Before
	public final void prepareWorlds4Test() {
		this.modelFactory.clearCache();
		this.world1 = this.modelFactory.newWorldBuilder().id(1000).name("test").worldLocation(WorldLocationType.EUROPE).build();
		this.world2 = this.modelFactory.newWorldBuilder().id(1001).name("test2").worldLocation(WorldLocationType.EUROPE).build();
		this.world3 = this.modelFactory.newWorldBuilder().id(1002).name("test3").worldLocation(WorldLocationType.EUROPE).build();
		this.world4 = this.modelFactory.newWorldBuilder().id(2000).name("abc").worldLocation(WorldLocationType.NORTH_AMERICA).build();
	}

	@Test
	public void testNewWorldEntityAndFindWorldEntityById() {
		final WorldEntityDAO dao = new WorldEntityDAO();

		assertTrue(dao.retrieveAllWorldEntities().size() == 0);
		assertFalse(dao.findWorldEntityById(1).isPresent());
		assertFalse(dao.findWorldEntityById(2).isPresent());
		assertFalse(dao.findWorldEntityById(3).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world1).isPresent());
		assertTrue(dao.retrieveAllWorldEntities().size() == 1);
		assertTrue(dao.findWorldEntityById(1).isPresent());
		assertFalse(dao.newWorldEntityOf(this.world1).isPresent());
		assertTrue(dao.findWorldEntityById(1).isPresent());
		assertTrue(dao.retrieveAllWorldEntities().size() == 1);
		assertTrue(dao.newWorldEntityOf(this.world2).isPresent());
		assertTrue(dao.retrieveAllWorldEntities().size() == 2);
		assertTrue(dao.findWorldEntityById(3).isPresent());
		assertFalse(dao.newWorldEntityOf(this.world2).isPresent());
		assertTrue(dao.findWorldEntityById(3).isPresent());
		assertTrue(dao.retrieveAllWorldEntities().size() == 2);
		assertTrue(dao.newWorldEntityOf(this.world3).isPresent());
		assertTrue(dao.findWorldEntityById(5).isPresent());
		assertFalse(dao.newWorldEntityOf(this.world3).isPresent());
		assertTrue(dao.findWorldEntityById(5).isPresent());

	}

	@Test
	public void testFindWorldEntityByName() {
		final WorldEntityDAO dao = new WorldEntityDAO();

		assertFalse(dao.findWorldEntityByName(YAGW2APIArenanet.INSTANCE.getCurrentLocale(), "test").isPresent());
		assertFalse(dao.findWorldEntityByName(YAGW2APIArenanet.INSTANCE.getCurrentLocale(), "test2").isPresent());
		assertFalse(dao.findWorldEntityByName(YAGW2APIArenanet.INSTANCE.getCurrentLocale(), "test3").isPresent());
		assertFalse(dao.findWorldEntityByName(YAGW2APIArenanet.INSTANCE.getCurrentLocale(), "test4").isPresent());
		assertTrue(dao.newWorldEntityOf(this.world1).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world2).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world3).isPresent());
		assertTrue(dao.findWorldEntityByName(YAGW2APIArenanet.INSTANCE.getCurrentLocale(), "test").isPresent());
		assertTrue(dao.findWorldEntityByName(YAGW2APIArenanet.INSTANCE.getCurrentLocale(), "test2").isPresent());
		assertTrue(dao.findWorldEntityByName(YAGW2APIArenanet.INSTANCE.getCurrentLocale(), "test3").isPresent());
		assertFalse(dao.findWorldEntityByName(YAGW2APIArenanet.INSTANCE.getCurrentLocale(), "test4").isPresent());
	}

	@Test
	public void testRetrieveAllWorldEntities() {
		final WorldEntityDAO dao = new WorldEntityDAO();

		assertTrue(dao.retrieveAllWorldEntities().size() == 0);
		assertTrue(dao.newWorldEntityOf(this.world1).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world2).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world3).isPresent());
		assertTrue(dao.retrieveAllWorldEntities().size() == 3);
		assertTrue(dao.newWorldEntityOf(this.world4).isPresent());
		assertTrue(dao.retrieveAllWorldEntities().size() == 4);
	}

	@Test
	public void testSearchWorldEntityByNamePart() {

		final WorldEntityDAO dao = new WorldEntityDAO();

		assertTrue(dao.retrieveAllWorldEntities().size() == 0);
		assertTrue(dao.newWorldEntityOf(this.world1).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world2).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world3).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world4).isPresent());
		assertTrue(dao.retrieveAllWorldEntities().size() == 4);
		final int resultCount = dao.searchWorldEntityByNamePart("est").size();
		assertEquals(3, resultCount);
	}

	@Test
	public void testFindWorldEntityByOriginId() {
		final WorldEntityDAO dao = new WorldEntityDAO();
		assertTrue(dao.retrieveAllWorldEntities().size() == 0);
		assertTrue(dao.newWorldEntityOf(this.world1).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world2).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world3).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world4).isPresent());
		assertTrue(dao.retrieveAllWorldEntities().size() == 4);
		assertTrue(dao.findWorldEntityByOriginId(2000).isPresent());
		assertEquals(dao.findWorldEntityById(4).get(), dao.findWorldEntityByOriginId(2000).get());
		assertFalse(dao.findWorldEntityByOriginId(3000).isPresent());
	}

	@Test
	public void testFindWorldEntityOf() {
		final WorldEntityDAO dao = new WorldEntityDAO();
		assertTrue(dao.retrieveAllWorldEntities().size() == 0);
		assertTrue(dao.newWorldEntityOf(this.world1).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world2).isPresent());
		assertTrue(dao.newWorldEntityOf(this.world3).isPresent());
		assertTrue(dao.retrieveAllWorldEntities().size() == 3);
		assertTrue(dao.findWorldEntityOf(this.world1).isPresent());
		assertEquals(dao.findWorldEntityById(1).get(), dao.findWorldEntityOf(this.world1).get());
		assertTrue(dao.findWorldEntityOf(this.world2).isPresent());
		assertEquals(dao.findWorldEntityById(2).get(), dao.findWorldEntityOf(this.world2).get());
		assertTrue(dao.findWorldEntityOf(this.world3).isPresent());
		assertEquals(dao.findWorldEntityById(3).get(), dao.findWorldEntityOf(this.world3).get());
		assertFalse(dao.findWorldEntityOf(this.world4).isPresent());
	}

	@Test
	public void testFindOrCreateWorldEntityOf() {
		final WorldEntityDAO dao = new WorldEntityDAO();
		assertTrue(dao.retrieveAllWorldEntities().size() == 0);
		assertNotNull(dao.findOrCreateWorldEntityOf(this.world1));
		assertTrue(dao.retrieveAllWorldEntities().size() == 1);
		assertNotNull(dao.findOrCreateWorldEntityOf(this.world1));
		assertTrue(dao.retrieveAllWorldEntities().size() == 1);
		assertEquals(dao.findOrCreateWorldEntityOf(this.world1), dao.findOrCreateWorldEntityOf(this.world1));
		assertTrue(dao.retrieveAllWorldEntities().size() == 1);
	}
}
