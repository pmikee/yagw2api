package model.wvw.events;

import com.google.common.base.Optional;

import model.IWorld;
import model.wvw.IWVWMap;
import model.wvw.IWVWMatch;
import model.wvw.IWVWScores;
import model.wvw.types.IWVWObjective;

public interface IWVWModelEventFactory {
	IWVWObjectiveCaptureEvent newObjectiveCapturedEvent(IWVWObjective source, IWorld newOwner, Optional<IWorld> previousOwner);
	IWVWMapScoresChangedEvent newMapScoresChangedEvent(IWVWScores source, IWVWMap map);
	IWVWMatchScoresChangedEvent newMatchScoresChangedEvent(IWVWScores source, IWVWMatch match);
}
