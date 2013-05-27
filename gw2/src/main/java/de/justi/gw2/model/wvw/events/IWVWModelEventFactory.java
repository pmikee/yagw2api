package de.justi.gw2.model.wvw.events;

import com.google.common.base.Optional;

import de.justi.gw2.model.IWorld;
import de.justi.gw2.model.wvw.IWVWMap;
import de.justi.gw2.model.wvw.IWVWMatch;
import de.justi.gw2.model.wvw.IWVWScores;
import de.justi.gw2.model.wvw.types.IWVWObjective;


public interface IWVWModelEventFactory {
	IWVWObjectiveCaptureEvent newObjectiveCapturedEvent(IWVWObjective source, IWorld newOwner, Optional<IWorld> previousOwner);
	IWVWMapScoresChangedEvent newMapScoresChangedEvent(IWVWScores source, IWVWMap map);
	IWVWMatchScoresChangedEvent newMatchScoresChangedEvent(IWVWScores source, IWVWMatch match);
}
