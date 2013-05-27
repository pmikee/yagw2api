package de.justi.gw2.model.wvw.events;

import com.google.common.base.Optional;

import de.justi.gw2.model.IWorld;
import de.justi.gw2.model.wvw.IWVWMap;
import de.justi.gw2.model.wvw.IWVWMatch;
import de.justi.gw2.model.wvw.IWVWObjective;
import de.justi.gw2.model.wvw.IWVWScores;

public interface IWVWModelEventFactory {
	IWVWObjectiveCaptureEvent newObjectiveCapturedEvent(IWVWObjective source, IWorld newOwner, Optional<IWorld> previousOwner);
	IWVWObjectiveEndOfBuffEvent newObjectiveEndOfBuffEvent(IWVWObjective source);
	
	IWVWMapScoresChangedEvent newMapScoresChangedEvent(IWVWScores source, int deltaRed, int deltaGreen, int deltaBlue, IWVWMap map);

	IWVWMatchScoresChangedEvent newMatchScoresChangedEvent(IWVWScores source, int deltaRed, int deltaGreen, int deltaBlue, IWVWMatch match);	
}
