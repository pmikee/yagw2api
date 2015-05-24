package de.justi.yagw2api.wrapper.map.domain.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import static org.mockito.Mockito.mock;

import org.junit.Test;

import de.justi.yagw2api.arenanet.MapFloorService;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;
import de.justi.yagwapi.common.Tuples;

public class DefaultMapFloorTilesTest {

	@Test
	public final void test() {
		for (int i = 0; i < 10; i++) {
			System.out.println(i + ": " + (1 << i));
		}
		System.out.println();
		System.out.println((1 << 6) * 256);
		System.out.println((1 << 7) * 256);
	}

	@Test
	public final void testGetTileTextureSize() {

		final DefaultMapFloor underTest = (DefaultMapFloor) DefaultMapFloor.builder(mock(MapFloorService.class), mock(MapDomainFactory.class)).continentId("99")
				.floorIndex(99).build();

		System.out.println(32768 / 2); // 0
		System.out.println(32768 / 4); // 1
		System.out.println(32768 / 8); // 2
		System.out.println(32768 / 16); // 3
		for (int zoom = 0; zoom <= 6; zoom++) {
			System.out.println(underTest.getTileTextureSize(zoom) + ": " + underTest.texture2Tile(18144, zoom));
		}

		System.out.println();
		System.out.println();
		System.out.println();

		System.out.println(underTest.overlaps(Tuples.of(0, 0, 10, 10), Tuples.of(0, 0, 10, 10)));
		System.out.println(underTest.overlaps(Tuples.of(1, 1, 9, 9), Tuples.of(0, 0, 10, 10)));
		System.out.println(underTest.overlaps(Tuples.of(0, 0, 10, 10), Tuples.of(10, 10, 20, 20)));
		System.out.println(underTest.overlaps(Tuples.of(0, 0, 10, 10), Tuples.of(-10, -10, 0, 0)));
		System.out.println(underTest.overlaps(Tuples.of(0, 0, 10, 10), Tuples.of(-20, -20, -10, -10)));
	}
}
