package de.justi.yagw2api.core.wrapper.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.IGuild;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWScores;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWInitializedMatchEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWModelEventFactory;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveUnclaimedEvent;

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
