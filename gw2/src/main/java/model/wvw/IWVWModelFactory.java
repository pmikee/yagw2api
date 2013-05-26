package model.wvw;

import model.wvw.types.IWVWObjective;

public interface IWVWModelFactory {
	// builders
	IWVWMap.IWVWMapBuilder newMapBuilder();
	IWVWObjective.IWVWObjectiveBuilder newObjectiveBuilder();
	IWVWMatch.IWVWMatchBuilder newMatchBuilder();
	
	// creation	
	IWVWScores newMapScores(IWVWMap map);
	IWVWScores newMatchScores(IWVWMatch match); 
}
