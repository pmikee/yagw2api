package de.justi.yagw2api.wrapper.wvw.event;

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
import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.guild.domain.Guild;
import de.justi.yagw2api.wrapper.world.domain.World;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMap;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMatch;
import de.justi.yagw2api.wrapper.wvw.domain.WVWObjective;
import de.justi.yagw2api.wrapper.wvw.domain.WVWScores;

public interface WVWEventFactory {
	WVWObjectiveUnclaimedEvent newObjectiveUnclaimedEvent(WVWObjective objective, Optional<Guild> previousClaimedByGuild);

	WVWObjectiveClaimedEvent newObjectiveClaimedEvent(WVWObjective objective, Guild claimingGuild, Optional<Guild> previousClaimedByGuild);

	WVWObjectiveCaptureEvent newObjectiveCapturedEvent(WVWObjective objective, World newOwner, Optional<World> previousOwner);

	WVWObjectiveEndOfBuffEvent newObjectiveEndOfBuffEvent(WVWObjective source);

	WVWMapScoresChangedEvent newMapScoresChangedEvent(WVWScores scores, int deltaRed, int deltaGreen, int deltaBlue, WVWMap map);

	WVWMatchScoresChangedEvent newMatchScoresChangedEvent(WVWScores scores, int deltaRed, int deltaGreen, int deltaBlue, WVWMatch match);

	WVWInitializedMatchEvent newInitializedMatchEvent(WVWMatch match);
}
