package de.justi.gw2.model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.gw2.model.wvw.IWVWMap;
import de.justi.gw2.model.wvw.IWVWMatch;
import de.justi.gw2.model.wvw.IWVWModelFactory;
import de.justi.gw2.model.wvw.IWVWObjective;
import de.justi.gw2.model.wvw.IWVWScores;

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
	public IWVWScores newMapScores(IWVWMap map) {
		return new WVWMapScores(checkNotNull(map));
	}

	@Override
	public IWVWMatch.IWVWMatchBuilder newMatchBuilder() {
		return new WVWMatch.WVWMatchBuilder();
	}

	@Override
	public IWVWScores newMatchScores(IWVWMatch match) {
		return new WVWMatchScores(checkNotNull(match));
	}
}