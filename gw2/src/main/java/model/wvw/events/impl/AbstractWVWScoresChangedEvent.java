package model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.AbstractEvent;
import model.wvw.IWVWScores;
import model.wvw.events.IWVWScoresChangedEvent;

abstract class AbstractWVWScoresChangedEvent extends AbstractEvent implements IWVWScoresChangedEvent {
	private final IWVWScores scores;
	
	public AbstractWVWScoresChangedEvent(IWVWScores scores) {
		super();
		checkNotNull(scores);
		this.scores = scores;
	}
	
	@Override
	public IWVWScores getScores() {
		return this.scores;
	}

}
