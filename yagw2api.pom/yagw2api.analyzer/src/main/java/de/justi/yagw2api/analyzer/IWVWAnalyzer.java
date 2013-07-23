package de.justi.yagw2api.analyzer;

import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.wrapper.IWVWMapListener;
import de.justi.yagw2api.wrapper.IWVWMatchListener;
import de.justi.yagw2api.wrapper.IWorld;

public interface IWVWAnalyzer extends IWVWMatchListener, IWVWMapListener {
	IWorldEntity worldEntityOf(IWorld world);
}
