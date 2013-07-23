package de.justi.yagw2api.wrapper;

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
