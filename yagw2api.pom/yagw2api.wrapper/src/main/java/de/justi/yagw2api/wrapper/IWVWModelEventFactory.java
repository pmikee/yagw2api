package de.justi.yagw2api.wrapper;

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

public interface IWVWModelEventFactory {
	IWVWObjectiveUnclaimedEvent newObjectiveUnclaimedEvent(IWVWObjective objective, Optional<IGuild> previousClaimedByGuild);

	IWVWObjectiveClaimedEvent newObjectiveClaimedEvent(IWVWObjective objective, IGuild claimingGuild, Optional<IGuild> previousClaimedByGuild);

	IWVWObjectiveCaptureEvent newObjectiveCapturedEvent(IWVWObjective objective, IWorld newOwner, Optional<IWorld> previousOwner);

	IWVWObjectiveEndOfBuffEvent newObjectiveEndOfBuffEvent(IWVWObjective source);

	IWVWMapScoresChangedEvent newMapScoresChangedEvent(IWVWScores scores, int deltaRed, int deltaGreen, int deltaBlue, IWVWMap map);

	IWVWMatchScoresChangedEvent newMatchScoresChangedEvent(IWVWScores scores, int deltaRed, int deltaGreen, int deltaBlue, IWVWMatch match);

	IWVWInitializedMatchEvent newInitializedMatchEvent(IWVWMatch match);
}
