package de.justi.yagw2api.wrapper.model.wvw;


public interface IWVWModelFactory {
	// builders
	IWVWMap.IWVWMapBuilder newMapBuilder();
	IWVWObjective.IWVWObjectiveBuilder newObjectiveBuilder();
	IWVWMatch.IWVWMatchBuilder newMatchBuilder();
	
	// creation	
	IWVWScores newMapScores(IWVWMap map);
	IWVWScores newMatchScores(IWVWMatch match); 
}
