package de.justi.yagw2api.analyzer.entities.wvw.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.justi.yagw2api.analyzer.AbstractAnalyzerTest;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWWorldEnityDAO;

public class WVWWorldEntityDAOTest extends AbstractAnalyzerTest {

	@Test
	public void testNewWorldEntity() {
		final IWVWWorldEnityDAO dao = new WVWWorldEntityDAO();

		assertFalse(dao.findWorldEntityById(1).isPresent());
		assertFalse(dao.findWorldEntityById(2).isPresent());
		assertFalse(dao.findWorldEntityById(3).isPresent());
		dao.newWorldEntity("test");
		dao.newWorldEntity("test2");
		dao.newWorldEntity("test3");
		assertTrue(dao.findWorldEntityById(1).isPresent());
		assertTrue(dao.findWorldEntityById(2).isPresent());
		assertTrue(dao.findWorldEntityById(3).isPresent());
	}

	@Test
	public void testFindWorldEntityByName() {
		final IWVWWorldEnityDAO dao = new WVWWorldEntityDAO();

		assertFalse(dao.findWorldEntityByName("test").isPresent());
		assertFalse(dao.findWorldEntityByName("test2").isPresent());
		assertFalse(dao.findWorldEntityByName("test3").isPresent());
		assertFalse(dao.findWorldEntityByName("test4").isPresent());
		dao.newWorldEntity("test");
		dao.newWorldEntity("test2");
		dao.newWorldEntity("test3");
		assertTrue(dao.findWorldEntityByName("test").isPresent());
		assertTrue(dao.findWorldEntityByName("test2").isPresent());
		assertTrue(dao.findWorldEntityByName("test3").isPresent());
		assertFalse(dao.findWorldEntityByName("test4").isPresent());
	}
}
