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

import de.justi.yagw2api.wrapper.domain.guild.Guild;
import de.justi.yagw2api.wrapper.domain.world.World;
import de.justi.yagw2api.wrapper.domain.wvw.WVWMap;
import de.justi.yagw2api.wrapper.domain.wvw.WVWMatch;
import de.justi.yagw2api.wrapper.domain.wvw.WVWObjective;
import de.justi.yagw2api.wrapper.domain.wvw.WVWScores;

public final class DefaultWVWModelEventFactory implements WVWModelEventFactory {

	@Override
	public WVWObjectiveCaptureEvent newObjectiveCapturedEvent(final WVWObjective source, final World newOwner, final Optional<World> previousOwner) {
		checkNotNull(source);
		checkNotNull(newOwner);
		checkNotNull(previousOwner);
		return new DefaultWVWObjectiveCaptureEvent(source, newOwner, previousOwner.orNull());
	}

	@Override
	public WVWMapScoresChangedEvent newMapScoresChangedEvent(final WVWScores scores, final int deltaRed, final int deltaGreen, final int deltaBlue, final WVWMap map) {
		checkNotNull(scores);
		checkNotNull(map);
		return new DefaultWVWMapScoresChangedEvent(scores, deltaRed, deltaGreen, deltaBlue, map);
	}

	@Override
	public WVWMatchScoresChangedEvent newMatchScoresChangedEvent(final WVWScores scores, final int deltaRed, final int deltaGreen, final int deltaBlue, final WVWMatch match) {
		checkNotNull(scores);
		checkNotNull(match);
		return new DefaultWVWMatchScoresChangedEvent(scores, deltaRed, deltaGreen, deltaBlue, match);
	}

	@Override
	public WVWObjectiveEndOfBuffEvent newObjectiveEndOfBuffEvent(final WVWObjective objective) {
		checkNotNull(objective);
		return new DefaultWVWObjectiveEndOfBuffEvent(objective);
	}

	@Override
	public WVWObjectiveClaimedEvent newObjectiveClaimedEvent(final WVWObjective objective, final Guild claimingGuild, final Optional<Guild> previousClaimedByGuild) {
		checkNotNull(objective);
		checkNotNull(claimingGuild);
		checkNotNull(previousClaimedByGuild);
		return new DefaultWVWObjectiveClaimedEvent(objective, claimingGuild, previousClaimedByGuild.orNull());
	}

	@Override
	public WVWInitializedMatchEvent newInitializedMatchEvent(final WVWMatch match) {
		checkNotNull(match);
		return new DefaultWVWInitializedMatchEvent(match);
	}

	@Override
	public WVWObjectiveUnclaimedEvent newObjectiveUnclaimedEvent(final WVWObjective objective, final Optional<Guild> previousClaimedByGuild) {
		checkNotNull(objective);
		return new DefaultWVWObjectiveUnclaimedEvent(objective, previousClaimedByGuild.orNull());
	}

}
