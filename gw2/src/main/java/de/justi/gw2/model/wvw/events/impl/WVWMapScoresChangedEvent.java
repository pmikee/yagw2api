package de.justi.gw2.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.gw2.model.wvw.IWVWMap;
import de.justi.gw2.model.wvw.IWVWScores;
import de.justi.gw2.model.wvw.events.IWVWMapScoresChangedEvent;

class WVWMapScoresChangedEvent extends AbstractWVWScoresChangedEvent implements IWVWMapScoresChangedEvent {
	private final IWVWMap map;
	
	public WVWMapScoresChangedEvent(IWVWScores scores, IWVWMap map) {
		super(checkNotNull(scores));
		this.map = checkNotNull(map);
	}

	@Override
	public IWVWMap getMap() {
		return this.map;
	}

}
