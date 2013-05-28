package de.justi.yagw2api.analyzer;

import de.justi.yagw2api.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.model.wvw.IWVWObjective;

public interface IWVWAnalyzer {
	void notifyAboutUpdatedMapScores(IWVWMap map);
	void notifyAboutUpdatedMatchScores(IWVWMatch match);
	void notifyAboutCapturedObjective(IWVWObjective objective);
	void notifyAboutEndOfBuffObjective(IWVWObjective objective);
}
