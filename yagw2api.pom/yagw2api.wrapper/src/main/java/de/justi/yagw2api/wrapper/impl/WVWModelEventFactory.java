package de.justi.yagw2api.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.IGuild;
import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWModelEventFactory;
import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveUnclaimedEvent;
import de.justi.yagw2api.wrapper.IWVWScores;
import de.justi.yagw2api.wrapper.IWorld;

final class WVWModelEventFactory implements IWVWModelEventFactory {

	@Override
	public IWVWObjectiveCaptureEvent newObjectiveCapturedEvent(IWVWObjective source, IWorld newOwner, Optional<IWorld> previousOwner) {
		checkNotNull(source);
		checkNotNull(newOwner);
		checkNotNull(previousOwner);
		return new WVWObjectiveCaptureEvent(source, newOwner, previousOwner.orNull());
	}

	@Override
	public IWVWMapScoresChangedEvent newMapScoresChangedEvent(IWVWScores scores, int deltaRed, int deltaGreen, int deltaBlue, IWVWMap map) {
		checkNotNull(scores);
		checkNotNull(map);
		return new WVWMapScoresChangedEvent(scores, deltaRed, deltaGreen, deltaBlue, map);
	}

	@Override
	public IWVWMatchScoresChangedEvent newMatchScoresChangedEvent(IWVWScores scores, int deltaRed, int deltaGreen, int deltaBlue, IWVWMatch match) {
		checkNotNull(scores);
		checkNotNull(match);
		return new WVWMatchScoresChanged(scores, deltaRed, deltaGreen, deltaBlue, match);
	}

	@Override
	public IWVWObjectiveEndOfBuffEvent newObjectiveEndOfBuffEvent(IWVWObjective objective) {
		checkNotNull(objective);
		return new WVWObjectiveEndOfBuffEvent(objective);
	}

	@Override
	public IWVWObjectiveClaimedEvent newObjectiveClaimedEvent(IWVWObjective objective, IGuild claimingGuild, Optional<IGuild> previousClaimedByGuild) {
		checkNotNull(objective);
		checkNotNull(claimingGuild);
		checkNotNull(previousClaimedByGuild);
		return new WVWObjectiveClaimedEvent(objective, claimingGuild, previousClaimedByGuild.orNull());
	}

	@Override
	public IWVWInitializedMatchEvent newInitializedMatchEvent(IWVWMatch match) {
		checkNotNull(match);
		return new WVWInitializedMatchEvent(match);
	}

	@Override
	public IWVWObjectiveUnclaimedEvent newObjectiveUnclaimedEvent(IWVWObjective objective, Optional<IGuild> previousClaimedByGuild) {
		checkNotNull(objective);
		return new WVWObjectiveUnclaimedEvent(objective, previousClaimedByGuild.orNull());
	}

}
