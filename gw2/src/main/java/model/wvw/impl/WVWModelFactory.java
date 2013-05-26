package model.wvw.impl;

import model.wvw.IWVWMap;
import model.wvw.IWVWMatch;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWScores;
import model.wvw.types.IWVWObjective;

public class WVWModelFactory implements IWVWModelFactory {

	@Override
	public IWVWMap.IWVWMapBuilder createMapBuilder() {
		return new WVWMap.WVWMapBuilder();
	}

	@Override
	public IWVWObjective.IWVWObjectiveBuilder createObjectiveBuilder() {
		return new WVWObjective.WVWObjectiveBuilder();
	}

	@Override
	public IWVWScores createScores() {
		return new WVWScores();
	}

	@Override
	public IWVWMatch.IWVWMatchBuilder createMatchBuilder() {
		return new WVWMatch.WVWMatchBuilder();
	}
}