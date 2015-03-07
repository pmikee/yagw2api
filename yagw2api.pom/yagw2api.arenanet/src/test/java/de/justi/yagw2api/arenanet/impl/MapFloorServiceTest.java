package de.justi.yagw2api.arenanet.impl;

import java.util.Locale;

import org.junit.Test;

public class MapFloorServiceTest {

	@Test
	public void test() {

		final MapDTOFactory dtoFactory = new MapDTOFactory();
		final MapFloorService service = new MapFloorService(dtoFactory);

		System.out.println(service.retrieveMapFloor(1, 1, Locale.GERMAN));
		System.out.println(service.retrieveMapFloor(1, 2, Locale.GERMAN));
		System.out.println(service.retrieveMapFloor(1, 3, Locale.GERMAN));
		System.out.println(service.retrieveMapFloor(1, 4, Locale.GERMAN));
		System.out.println(service.retrieveMapFloor(1, 5, Locale.GERMAN));
		System.out.println(service.retrieveMapFloor(1, 6, Locale.GERMAN));
		System.out.println(service.retrieveMapFloor(1, 7, Locale.GERMAN));
	}
}
