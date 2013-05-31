package de.justi.yagw2api.analyzer;

import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.core.wrapper.IWVWMapListener;
import de.justi.yagw2api.core.wrapper.IWVWMatchListener;
import de.justi.yagw2api.core.wrapper.model.IWorld;

public interface IWVWAnalyzer extends IWVWMatchListener, IWVWMapListener {
	IWorldEntity worldEntityOf(IWorld world);
}
