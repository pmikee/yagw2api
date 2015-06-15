package de.justi.yagw2api.wrapper.wvw.event.impl;

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

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.guild.domain.Guild;
import de.justi.yagw2api.wrapper.wvw.domain.WVWObjective;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveClaimedEvent;

final class DefaultWVWObjectiveClaimedEvent extends AbstractWVWObjectiveEvent implements WVWObjectiveClaimedEvent {

	private final Guild claimingGuild;
	private final Optional<Guild> previousClaimedByGuild;

	public DefaultWVWObjectiveClaimedEvent(final WVWObjective source, final Guild claimingGuild, final Guild previousClaimedByGuild) {
		super(source);
		this.claimingGuild = checkNotNull(claimingGuild);
		this.previousClaimedByGuild = Optional.fromNullable(previousClaimedByGuild);
	}

	@Override
	public Guild getClaimingGuild() {
		return this.claimingGuild;
	}

	@Override
	public Optional<Guild> previousClaimedByGuild() {
		return this.previousClaimedByGuild;
	}

}
