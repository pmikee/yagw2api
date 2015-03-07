package de.justi.yagw2api.wrapper.impl;

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

import de.justi.yagw2api.wrapper.IGuild;
import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWVWObjectiveClaimedEvent;

final class WVWObjectiveClaimedEvent extends AbstractWVWObjectiveEvent implements IWVWObjectiveClaimedEvent {

	private final IGuild claimingGuild;
	private final Optional<IGuild> previousClaimedByGuild;

	public WVWObjectiveClaimedEvent(final IWVWObjective source, final IGuild claimingGuild, final IGuild previousClaimedByGuild) {
		super(source);
		this.claimingGuild = checkNotNull(claimingGuild);
		this.previousClaimedByGuild = Optional.fromNullable(previousClaimedByGuild);
	}

	@Override
	public IGuild getClaimingGuild() {
		return this.claimingGuild;
	}

	@Override
	public Optional<IGuild> previousClaimedByGuild() {
		return this.previousClaimedByGuild;
	}

}
