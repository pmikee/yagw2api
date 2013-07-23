package de.justi.yagw2api.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWModelFactory;
import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWVWScores;

final class WVWModelFactory implements IWVWModelFactory {

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