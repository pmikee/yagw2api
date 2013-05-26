package model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.IWorld;
import model.wvw.IWVWMap;
import model.wvw.IWVWMatch;
import model.wvw.IWVWScores;
import model.wvw.events.IWVWMapScoresChangedEvent;
import model.wvw.events.IWVWMatchScoresChangedEvent;
import model.wvw.events.IWVWModelEventFactory;
import model.wvw.events.IWVWObjectiveCaptureEvent;
import model.wvw.types.IWVWObjective;

import com.google.common.base.Optional;

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
