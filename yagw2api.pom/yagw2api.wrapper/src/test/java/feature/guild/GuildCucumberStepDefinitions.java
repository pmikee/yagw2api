package feature.guild;

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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import cucumber.api.Scenario;
import cucumber.api.java8.En;
import de.justi.yagw2api.arenanet.v1.DelegatingGuildService;
import de.justi.yagw2api.arenanet.v1.GuildService;
import de.justi.yagw2api.arenanet.v1.YAGW2APIArenanetV1;
import de.justi.yagw2api.arenanet.v1.dto.guild.GuildDetailsDTO;
import de.justi.yagw2api.wrapper.guild.DefaultGuildWrapper;
import de.justi.yagw2api.wrapper.guild.GuildWrapper;
import de.justi.yagw2api.wrapper.guild.domain.Guild;
import de.justi.yagw2api.wrapper.guild.domain.GuildDomainFactory;
import de.justi.yagw2api.wrapper.guild.domain.NoSuchGuildException;
import de.justi.yagw2api.wrapper.guild.domain.impl.DefaultGuildDomainFactory;

public class GuildCucumberStepDefinitions implements En {

	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(GuildCucumberStepDefinitions.class);

	// STATIC FIELD
	private static GuildCucumberStepDefinitions lastInstance;

	// FIELDS
	private Map<String, GuildDetailsDTO> givenGuildDetails = Maps.newHashMap();
	private GuildService service;
	private GuildDomainFactory domainFactory;
	private GuildWrapper wrapper;
	private Guild retrievedGuild;
	private NoSuchGuildException expectedNoSuchGuildException;

	// CONSTRUCTOR
	public GuildCucumberStepDefinitions() {
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
		this.Given("^the real guild service$", () -> {
			this.service = spy(new DelegatingGuildService(YAGW2APIArenanetV1.getInstance().getGuildService()));
		});

		this.Given("^a guild service that knows no guild at all$", () -> {
			this.service = mock(GuildService.class);
			doReturn(Optional.absent()).when(this.service).retrieveGuildDetails(anyString());
		});
		this.Given("^a guild wrapper under test$", () -> {
			this.wrapper = new DefaultGuildWrapper(this.service, this.domainFactory);
		});
		this.Given("^a working guild domain factory$", () -> {
			this.domainFactory = new DefaultGuildDomainFactory();
		});
		this.Given("^a guild service that knows any guild whose id is not \"(.*?)\"$", (final String id) -> {
			final GuildDetailsDTO guildDetails = mock(GuildDetailsDTO.class);
			this.service = Mockito.mock(GuildService.class);
			doReturn(Optional.of(guildDetails)).when(this.service).retrieveGuildDetails(Mockito.anyString());
			doReturn(Optional.absent()).when(this.service).retrieveGuildDetails(same(id));
		});
		this.Given("^a service that knows only given guilds$", () -> {
			this.service = Mockito.mock(GuildService.class);
			doAnswer(invocation -> {
				final String id = invocation.getArgumentAt(0, String.class);
				if (this.givenGuildDetails.containsKey(id)) {
					return Optional.of(this.givenGuildDetails.get(id));
				} else {
					return Optional.absent();
				}
			}).when(this.service).retrieveGuildDetails(anyString());
		});
		this.Given(".+ guild with id=\"(.*?)\", name=\"(.*?)\" and tag=\"(.*?)\"$", (final String id, final String name, final String tag) -> {
			final GuildDetailsDTO guildDetails = mock(GuildDetailsDTO.class);
			doReturn(id).when(guildDetails).getId();
			doReturn(name).when(guildDetails).getName();
			doReturn(tag).when(guildDetails).getTag();
			doReturn(Optional.absent()).when(guildDetails).getEmblem();
			this.givenGuildDetails.put(id, guildDetails);
		});
	}

	private void configureWhen() {
		this.When("^the user tries to retrieve the guild with id=\"(.*?)\".*?$", (final String id) -> {
			try {
				this.retrievedGuild = this.wrapper.getGuild(id);
			} catch (NoSuchGuildException e) {
				this.expectedNoSuchGuildException = e;
			}
		});
	}

	private void configureThen() {
		this.Then("^an exception should be thrown that indicates that there is no such guild$", () -> {
			assertThat(this.expectedNoSuchGuildException, is(notNullValue()));
			assertThat(this.retrievedGuild, is(nullValue()));
		});
		this.Then("^the guild with id=\"(.*?)\", name=\"(.*?)\" and tag=\"(.*?)\" has been retrieved$", (final String id, final String name, final String tag) -> {
			assertThat(this.retrievedGuild, is(notNullValue()));
			assertThat(this.retrievedGuild.getId(), is(equalTo(id)));
			assertThat(this.retrievedGuild.getName(), is(equalTo(name)));
			assertThat(this.retrievedGuild.getTag(), is(equalTo(tag)));
		});
		this.Then("^the service has been questioned for the guild with id=\"(.*?)\" exactly '(\\d)+' times$", (final String id, final Integer times) -> {
			verify(this.service, times(times)).retrieveGuildDetails(eq(id));
		});
	}
}
