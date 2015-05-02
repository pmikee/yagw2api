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
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Map;

import org.mockito.Mockito;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.justi.yagw2api.arenanet.GuildService;
import de.justi.yagw2api.arenanet.dto.guild.GuildDetailsDTO;
import de.justi.yagw2api.wrapper.guild.DefaultGuildWrapper;
import de.justi.yagw2api.wrapper.guild.GuildWrapper;
import de.justi.yagw2api.wrapper.guild.domain.Guild;
import de.justi.yagw2api.wrapper.guild.domain.GuildDomainFactory;
import de.justi.yagw2api.wrapper.guild.domain.NoSuchGuildException;
import de.justi.yagw2api.wrapper.guild.domain.impl.DefaultGuildDomainFactory;

public class GuildCucumberStepDefinitions {
	private Map<String, GuildDetailsDTO> givenGuildDetails = Maps.newHashMap();
	private GuildService service;
	private GuildDomainFactory domainFactory;
	private GuildWrapper wrapper;
	private Guild retrievedGuild;
	private NoSuchGuildException expectedNoSuchGuildException;

	@Given("^a guild service that knows no guild at all$")
	public void a_guild_service_that_knows_no_guild_at_all() throws Throwable {
		this.service = mock(GuildService.class);
		doReturn(Optional.absent()).when(this.service).retrieveGuildDetails(anyString());
	}

	@Given("^a guild 'wrapper' under test$")
	public void a_guild_wrapper() throws Throwable {
		this.wrapper = new DefaultGuildWrapper(this.service, this.domainFactory);
	}

	@Given("^a working guild domain factory$")
	public void a_working_guild_domain_factory() throws Throwable {
		this.domainFactory = new DefaultGuildDomainFactory();
	}

	@Given("^a guild service that knows any guild whose id is not '([A-Za-z0-9-]+)'$")
	public void a_guild_service_that_knows_any_guild_whose_id_is_not(final String id) throws Throwable {
		final GuildDetailsDTO guildDetails = mock(GuildDetailsDTO.class);
		this.service = Mockito.mock(GuildService.class);
		doReturn(Optional.of(guildDetails)).when(this.service).retrieveGuildDetails(Mockito.anyString());
		doReturn(Optional.absent()).when(this.service).retrieveGuildDetails(same(id));
	}

	@Given("^a service that knows only given guilds$")
	public void a_service_that_knows_only_given_guilds() throws Throwable {
		this.service = Mockito.mock(GuildService.class);
		doAnswer(invocation -> {
			final String id = invocation.getArgumentAt(0, String.class);
			if (this.givenGuildDetails.containsKey(id)) {
				return Optional.of(this.givenGuildDetails.get(id));
			} else {
				return Optional.absent();
			}
		}).when(this.service).retrieveGuildDetails(anyString());
	}

	@Given(".+ guild with id='([A-Za-z0-9-]+)', name='(.*)' and tag='([A-Za-z0-9-]+)'$")
	public void a_guild_with_id_name_and_tag(final String id, final String name, final String tag) throws Throwable {
		final GuildDetailsDTO guildDetails = mock(GuildDetailsDTO.class);
		doReturn(id).when(guildDetails).getId();
		doReturn(name).when(guildDetails).getName();
		doReturn(tag).when(guildDetails).getTag();
		doReturn(Optional.absent()).when(guildDetails).getEmblem();
		this.givenGuildDetails.put(id, guildDetails);
	}

	@When("^the user tries to retrieve the guild with id='([A-Za-z0-9-]+)'$")
	public void the_user_tries_to_retrieve_the_guild_with_id(final String id) throws Throwable {
		try {
			this.retrievedGuild = this.wrapper.getGuild(id);
		} catch (NoSuchGuildException e) {
			this.expectedNoSuchGuildException = e;
		}
	}

	@Then("^an exception should be thrown that indicates that there is no such guild$")
	public void an_exception_should_be_thrown_that_indicates_that_there_is_no_such_guild() throws Throwable {
		assertThat(this.expectedNoSuchGuildException, is(notNullValue()));
		assertThat(this.retrievedGuild, is(nullValue()));
	}

	@Then("^the guild with id='([A-Za-z0-9-]+)', name='(.*)' and tag='([A-Za-z0-9-]+)' has been retrieved$")
	public void the_guild_with_id_has_been_retrieved(final String id, final String name, final String tag) throws Throwable {
		assertThat(this.retrievedGuild, is(notNullValue()));
		assertThat(this.retrievedGuild.getId(), is(equalTo(id)));
		assertThat(this.retrievedGuild.getName(), is(equalTo(name)));
		assertThat(this.retrievedGuild.getTag(), is(equalTo(tag)));
	}
}
