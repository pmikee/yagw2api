package de.justi.yagw2api.wrapper.guild.domain.impl;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.justi.yagw2api.wrapper.guild.domain.Guild;
import de.justi.yagw2api.wrapper.guild.domain.GuildDomainFactory;
import de.justi.yagw2api.wrapper.guild.domain.Guild.GuildBuilder;

public final class DefaultGuildDomainFactory implements GuildDomainFactory {
	// CONSTS
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGuildDomainFactory.class);

	// FIELDS

	// CONSTRUCTOR
	public DefaultGuildDomainFactory() {
	}

	// METHODS
	@Override
	public GuildBuilder newGuildBuilder() {
		return DefaultGuild.builder();
	}

}
