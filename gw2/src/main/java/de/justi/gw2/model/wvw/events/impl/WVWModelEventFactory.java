package de.justi.gw2.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;

import de.justi.gw2.model.IWorld;
import de.justi.gw2.model.wvw.IWVWMap;
import de.justi.gw2.model.wvw.IWVWMatch;
import de.justi.gw2.model.wvw.IWVWScores;
import de.justi.gw2.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.gw2.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.gw2.model.wvw.events.IWVWModelEventFactory;
import de.justi.gw2.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.gw2.model.wvw.types.IWVWObjective;

class WVWModelEventFactory implements IWVWModelEventFactory {

	@Override
	public IWVWObjectiveCaptureEvent newObjectiveCapturedEvent(IWVWObjective source, IWorld newOwner, Optional<IWorld> previousOwner) {
		checkNotNull(source);
		checkNotNull(newOwner);
		checkNotNull(previousOwner);
		return new WVWObjectiveCaptureEvent(source, newOwner, previousOwner.orNull());
	}

	@Override
	public IWVWMapScoresChangedEvent newMapScoresChangedEvent(IWVWScores source, IWVWMap map) {
		checkNotNull(source);
		checkNotNull(map);
		return new WVWMapScoresChangedEvent(source, map);
	}

	@Override
	public IWVWMatchScoresChangedEvent newMatchScoresChangedEvent(IWVWScores source, IWVWMatch match) {
		checkNotNull(source);
		checkNotNull(match);
		return new WVWMatchScoresChanged(source, match);
	}

}
