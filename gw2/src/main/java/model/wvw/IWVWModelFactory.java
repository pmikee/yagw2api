package model.wvw;

import model.wvw.types.IWVWObjective;

public interface IWVWModelFactory {
	// builders
	IWVWMap.IWVWMapBuilder createMapBuilder();
	IWVWObjective.IWVWObjectiveBuilder createObjectiveBuilder();
	IWVWMatch.IWVWMatchBuilder createMatchBuilder();
	
	// creation	
	IWVWScores createScores();
}
