package de.justi.yagw2api.arenanet.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import de.justi.yagw2api.arenanet.IMapFloorDTO;
import de.justi.yagw2api.test.AbstractYAGW2APITest;

@RunWith(Parameterized.class)
public class MapFloorServiceIT extends AbstractYAGW2APITest {
	// STATIC
	@Parameters(name = "continentId={0}&floor={1}&lang={2}")
	public static Collection<Object[]> parameters() {
		//@formatter:off
		return ImmutableList.copyOf(new Object[][]{
				{ 1, 1, Locale.GERMAN },
				{ 1, 1, Locale.ENGLISH },
				{ 1, 1, Locale.FRENCH },
				{ 1, 2, Locale.GERMAN },
				{ 1, 2, Locale.ENGLISH },
				{ 1, 2, Locale.FRENCH },
				{ 2, 1, Locale.GERMAN },
				{ 2, 1, Locale.ENGLISH },
				{ 2, 1, Locale.FRENCH }				
		});
		//@formatter:on
	}

	// FIELDS
	private final int continentId;
	private final int floor;
	private final Locale locale;

	// CONSTRUCTOR
	public MapFloorServiceIT(final int continentId, final int floor, final Locale locale) {
		this.continentId = continentId;
		this.floor = floor;
		this.locale = checkNotNull(locale, "missing locale");
	}

	// METHODS
	@Test
	public void testRetrieveMapFloor() {

		final MapDTOFactory dtoFactory = new MapDTOFactory();
		final MapFloorService service = new MapFloorService(dtoFactory);

		Optional<IMapFloorDTO> floor = service.retrieveMapFloor(this.continentId, this.floor, this.locale);

		assertThat(floor.isPresent(), is(true));
		assertThat(floor.get().getTextureDimension().getValue1().get(), is(greaterThan(0)));
		assertThat(floor.get().getTextureDimension().getValue2().get(), is(greaterThan(0)));
	}
}
