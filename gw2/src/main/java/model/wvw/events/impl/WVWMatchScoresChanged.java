package model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.wvw.IWVWMatch;
import model.wvw.IWVWScores;
import model.wvw.events.IWVWMatchScoresChangedEvent;

class WVWMatchScoresChanged extends AbstractWVWScoresChangedEvent implements IWVWMatchScoresChangedEvent {
	private final IWVWMatch match;
	public WVWMatchScoresChanged(IWVWScores scores, IWVWMatch match) {
		super(checkNotNull(scores));
		this.match = checkNotNull(match);
	}

	@Override
	public IWVWMatch getMatch() {
		return this.match;
	}

}
