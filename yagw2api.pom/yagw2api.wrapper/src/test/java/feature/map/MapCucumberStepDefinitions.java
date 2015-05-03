package feature.map;

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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import cucumber.api.Scenario;
import cucumber.api.java8.En;
import de.justi.yagw2api.arenanet.MapContinentService;
import de.justi.yagw2api.arenanet.dto.map.MapContinentWithIdDTO;
import de.justi.yagw2api.wrapper.map.DefaultMapWrapper;
import de.justi.yagw2api.wrapper.map.MapWrapper;
import de.justi.yagw2api.wrapper.map.domain.Continent;
import de.justi.yagwapi.common.Tuples;

public class MapCucumberStepDefinitions implements En {

	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(MapCucumberStepDefinitions.class);

	// STATIC FIELD
	private static MapCucumberStepDefinitions lastInstance;

	// FIELDS
	private MapContinentService mapContinentService;
	private MapWrapper mapWrapper;
	private Iterable<Continent> retrievedContinents;
	private List<MapContinentWithIdDTO> givenMapContinents = Lists.newArrayList();

	// CONSTRUCTOR
	public MapCucumberStepDefinitions() {
		this.configureBefore();
		this.configureGiven();
		this.configureWhen();
		this.configureThen();
	}

	private void configureBefore() {
		this.Before((final Scenario scenario) -> {
			assertThat(this, is(not(sameInstance(lastInstance))));
			lastInstance = this;
		});
	}

	private void configureGiven() {

		this.Given("^a map continent service that knows no continent at all$", () -> {
			this.mapContinentService = Mockito.mock(MapContinentService.class);
			doReturn(ImmutableList.of()).when(this.mapContinentService).retrieveAllContinents();
		});

		this.Given("^a map continent service that knows the given continents$", () -> {
			this.mapContinentService = Mockito.mock(MapContinentService.class);
			doAnswer(invocation -> {
				return this.givenMapContinents;
			}).when(this.mapContinentService).retrieveAllContinents();
		});
		this.Given("^.+ continent with id=\"(.*?)\" and name=\"(.*?)\"$", (final String id, final String name) -> {
			final MapContinentWithIdDTO dto = Mockito.mock(MapContinentWithIdDTO.class);
			doReturn(id).when(dto).getId();
			doReturn(name).when(dto).getName();
			doReturn(ImmutableSet.of()).when(dto).getFloors();
			doReturn(Tuples.of(0, 0)).when(dto).getDimension();
			this.givenMapContinents.add(dto);
		});
		this.Given("^a continent wrapper under test$", () -> {
			this.mapWrapper = new DefaultMapWrapper(this.mapContinentService);
		});
	}

	private void configureWhen() {
		this.Given("^the user tries to retrieve all continents$", () -> {
			this.retrievedContinents = this.mapWrapper.getContinents();
		});
	}

	private void configureThen() {
		this.Then("^'(\\d+)' continents have been retrieved$", (final Integer count) -> {
			assertThat(this.retrievedContinents, is(iterableWithSize(count)));
		});
		this.Then("^continent with id=\"(.*?)\" and name=\"(.*?)\" is one of them$", (final String id, final String name) -> {
			assertThat(Iterables.filter(this.retrievedContinents, (continent) -> {
				return continent.getId().equals(id) && continent.getName().equals(name);
			}), is(iterableWithSize(1)));
		});
	}
}
