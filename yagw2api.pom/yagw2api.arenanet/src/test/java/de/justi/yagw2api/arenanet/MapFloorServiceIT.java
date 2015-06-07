package de.justi.yagw2api.arenanet;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import de.justi.yagw2api.arenanet.dto.map.DefaultMapDTOFactory;
import de.justi.yagw2api.arenanet.dto.map.MapFloorDTO;
import de.justi.yagw2api.test.AbstractYAGW2APITest;

@RunWith(Parameterized.class)
public final class MapFloorServiceIT extends AbstractYAGW2APITest {
	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(MapFloorServiceIT.class);

	// STATIC
	@Parameters(name = "continentId={0}&floor={1}&lang={2}")
	public static Collection<Object[]> parameters() {
		//@formatter:off
		return ImmutableList.copyOf(new Object[][]{
				{ "1", "1", Locale.GERMAN },
				{ "1", "1", Locale.FRENCH },
				{ "1", "2", Locale.ENGLISH },
				{ "2", "3", Locale.GERMAN },
				{ "2", "1", Locale.FRENCH }
		});
		//@formatter:on
	}

	// FIELDS
	private final String continentId;
	private final String floorIndex;
	private final Locale locale;

	// CONSTRUCTOR
	public MapFloorServiceIT(final String continentId, final String floorIndex, final Locale locale) {
		this.continentId = checkNotNull(continentId, "missing continentId");
		this.floorIndex = checkNotNull(floorIndex, "missing floorIndex");
		this.locale = checkNotNull(locale, "missing locale");
	}

	// METHODS
	@Test
	public void testRetrieveMapFloor() {

		final DefaultMapDTOFactory dtoFactory = new DefaultMapDTOFactory();
		final DefaultMapFloorService service = new DefaultMapFloorService(Locale.GERMANY, dtoFactory);

		final Optional<MapFloorDTO> floor = service.retrieveMapFloor(this.continentId, this.floorIndex, this.locale);

		assertThat(floor.isPresent(), is(true));
		assertThat(floor.get().getTextureDimension().v1(), is(greaterThan(0)));
		assertThat(floor.get().getTextureDimension().v2(), is(greaterThan(0)));
		assertThat(floor.get().getRegions().size(), is(greaterThan(0)));
	}
}
