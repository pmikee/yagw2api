package de.justi.yagw2api.core.wrapper.model.wvw.events;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWScores;

public interface IWVWModelEventFactory {
	IWVWObjectiveCaptureEvent newObjectiveCapturedEvent(IWVWObjective source, IWorld newOwner, Optional<IWorld> previousOwner);
	IWVWObjectiveEndOfBuffEvent newObjectiveEndOfBuffEvent(IWVWObjective source);
	
	IWVWMapScoresChangedEvent newMapScoresChangedEvent(IWVWScores source, int deltaRed, int deltaGreen, int deltaBlue, IWVWMap map);

	IWVWMatchScoresChangedEvent newMatchScoresChangedEvent(IWVWScores source, int deltaRed, int deltaGreen, int deltaBlue, IWVWMatch match);	
}
