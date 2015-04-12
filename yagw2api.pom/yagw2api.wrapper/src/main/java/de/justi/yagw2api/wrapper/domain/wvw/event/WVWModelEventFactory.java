package de.justi.yagw2api.wrapper.domain.wvw.event;

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

import de.justi.yagw2api.wrapper.domain.guild.IGuild;
import de.justi.yagw2api.wrapper.domain.world.IWorld;
import de.justi.yagw2api.wrapper.domain.wvw.IWVWMap;
import de.justi.yagw2api.wrapper.domain.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.domain.wvw.IWVWObjective;
import de.justi.yagw2api.wrapper.domain.wvw.IWVWScores;

public final class WVWModelEventFactory implements IWVWModelEventFactory {

	@Override
	public IWVWObjectiveCaptureEvent newObjectiveCapturedEvent(final IWVWObjective source, final IWorld newOwner, final Optional<IWorld> previousOwner) {
		checkNotNull(source);
		checkNotNull(newOwner);
		checkNotNull(previousOwner);
		return new WVWObjectiveCaptureEvent(source, newOwner, previousOwner.orNull());
	}

	@Override
	public IWVWMapScoresChangedEvent newMapScoresChangedEvent(final IWVWScores scores, final int deltaRed, final int deltaGreen, final int deltaBlue, final IWVWMap map) {
		checkNotNull(scores);
		checkNotNull(map);
		return new WVWMapScoresChangedEvent(scores, deltaRed, deltaGreen, deltaBlue, map);
	}

	@Override
	public IWVWMatchScoresChangedEvent newMatchScoresChangedEvent(final IWVWScores scores, final int deltaRed, final int deltaGreen, final int deltaBlue, final IWVWMatch match) {
		checkNotNull(scores);
		checkNotNull(match);
		return new WVWMatchScoresChanged(scores, deltaRed, deltaGreen, deltaBlue, match);
	}

	@Override
	public IWVWObjectiveEndOfBuffEvent newObjectiveEndOfBuffEvent(final IWVWObjective objective) {
		checkNotNull(objective);
		return new WVWObjectiveEndOfBuffEvent(objective);
	}

	@Override
	public IWVWObjectiveClaimedEvent newObjectiveClaimedEvent(final IWVWObjective objective, final IGuild claimingGuild, final Optional<IGuild> previousClaimedByGuild) {
		checkNotNull(objective);
		checkNotNull(claimingGuild);
		checkNotNull(previousClaimedByGuild);
		return new WVWObjectiveClaimedEvent(objective, claimingGuild, previousClaimedByGuild.orNull());
	}

	@Override
	public IWVWInitializedMatchEvent newInitializedMatchEvent(final IWVWMatch match) {
		checkNotNull(match);
		return new WVWInitializedMatchEvent(match);
	}

	@Override
	public IWVWObjectiveUnclaimedEvent newObjectiveUnclaimedEvent(final IWVWObjective objective, final Optional<IGuild> previousClaimedByGuild) {
		checkNotNull(objective);
		return new WVWObjectiveUnclaimedEvent(objective, previousClaimedByGuild.orNull());
	}

}
