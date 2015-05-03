package de.justi.yagw2api.wrapper.wvw.domain.impl;

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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;

import de.justi.yagw2api.wrapper.guild.GuildWrapper;
import de.justi.yagw2api.wrapper.world.domain.WorldDomainFactory;
import de.justi.yagw2api.wrapper.wvw.domain.WVWDomainFactory;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMap;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMatch;
import de.justi.yagw2api.wrapper.wvw.domain.WVWObjective;
import de.justi.yagw2api.wrapper.wvw.domain.WVWScores;

public final class DefaultWVWDomainFactory implements WVWDomainFactory {
	// FIELDS
	private final GuildWrapper guildWrapper;
	private final WorldDomainFactory worldDomainFactory;

	// CONSTRUCTOR
	@Inject
	public DefaultWVWDomainFactory(final GuildWrapper guildWrapper, final WorldDomainFactory worldDomainFactory) {
		this.guildWrapper = checkNotNull(guildWrapper, "missing guildWrapper");
		this.worldDomainFactory = checkNotNull(worldDomainFactory, "missing worldDomainFactory");
	}

	// METHODS
	@Override
	public WVWMap.WVWMapBuilder newMapBuilder() {
		return new DefaultWVWMap.DefaultWVWMapBuilder();
	}

	@Override
	public WVWObjective.WVWObjectiveBuilder newObjectiveBuilder() {
		return new DefaultWVWObjective.DefaultWVWObjectiveBuilder(this.guildWrapper);
	}

	@Override
	public WVWScores newMapScores(final WVWMap map) {
		return new DefaultWVWMapScores(checkNotNull(map));
	}

	@Override
	public WVWMatch.WVWMatchBuilder newMatchBuilder() {
		return new DefaultWVWMatch.DefaultWVWMatchBuilder(this.worldDomainFactory);
	}

	@Override
	public WVWScores newMatchScores(final WVWMatch match) {
		return new DefaultWVWMatchScores(checkNotNull(match));
	}
}
