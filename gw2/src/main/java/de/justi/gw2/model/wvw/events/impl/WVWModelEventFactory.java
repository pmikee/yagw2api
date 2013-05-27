package de.justi.gw2.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;

import de.justi.gw2.model.IWorld;
import de.justi.gw2.model.wvw.IWVWMap;
import de.justi.gw2.model.wvw.IWVWMatch;
import de.justi.gw2.model.wvw.IWVWObjective;
import de.justi.gw2.model.wvw.IWVWScores;
import de.justi.gw2.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.gw2.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.gw2.model.wvw.events.IWVWModelEventFactory;
import de.justi.gw2.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.gw2.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

class WVWModelEventFactory implements IWVWModelEventFactory {

	@Override
	public IWVWObjectiveCaptureEvent newObjectiveCapturedEvent(IWVWObjective source, IWorld newOwner, Optional<IWorld> previousOwner) {
		checkNotNull(source);
		checkNotNull(newOwner);
		checkNotNull(previousOwner);
		return new WVWObjectiveCaptureEvent(source, newOwner, previousOwner.orNull());
	}

	@Override
	public IWVWMapScoresChangedEvent newMapScoresChangedEvent(IWVWScores source, int deltaRed, int deltaGreen, int deltaBlue, IWVWMap map) {
		checkNotNull(source);
		checkNotNull(map);
		return new WVWMapScoresChangedEvent(source, deltaRed, deltaGreen, deltaBlue, map);
	}

	@Override
	public IWVWMatchScoresChangedEvent newMatchScoresChangedEvent(IWVWScores source, int deltaRed, int deltaGreen, int deltaBlue, IWVWMatch match) {
		checkNotNull(source);
		checkNotNull(match);
		return new WVWMatchScoresChanged(source, deltaRed, deltaGreen, deltaBlue, match);
	}

	@Override
	public IWVWObjectiveEndOfBuffEvent newObjectiveEndOfBuffEvent(IWVWObjective source) {
		checkNotNull(source);
		return new WVWObjectiveEndOfBuffEvent(source);
	}

}
