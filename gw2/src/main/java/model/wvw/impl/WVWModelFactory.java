package model.wvw.impl;

import model.wvw.IWVWMap;
import model.wvw.IWVWMatch;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWScores;
import model.wvw.types.IWVWObjective;

public class WVWModelFactory implements IWVWModelFactory {

	@Override
	public IWVWMap.IWVWMapBuilder newMapBuilder() {
		return new WVWMap.WVWMapBuilder();
	}

	@Override
	public IWVWObjective.IWVWObjectiveBuilder newObjectiveBuilder() {
		return new WVWObjective.WVWObjectiveBuilder();
	}

	@Override
	public IWVWScores newScores() {
		return new WVWScores();
	}

	@Override
	public IWVWMatch.IWVWMatchBuilder newMatchBuilder() {
		return new WVWMatch.WVWMatchBuilder();
	}
}