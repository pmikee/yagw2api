package de.justi.yagw2api.wrapper.model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.model.wvw.IWVWModelFactory;
import de.justi.yagw2api.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.wrapper.model.wvw.IWVWScores;

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