package de.justi.gw2.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.gw2.model.AbstractEvent;
import de.justi.gw2.model.wvw.IWVWScores;
import de.justi.gw2.model.wvw.events.IWVWScoresChangedEvent;

abstract class AbstractWVWScoresChangedEvent extends AbstractEvent implements IWVWScoresChangedEvent {
	private final IWVWScores scores;
	private final int deltaRed;
	private final int deltaGreen;
	private final int deltaBlue;
	
	
	public AbstractWVWScoresChangedEvent(IWVWScores scores, int deltaRed, int deltaGreen, int deltaBlue) {
		super();
		checkNotNull(scores);
		checkArgument(deltaRed >= 0);
		checkArgument(deltaGreen >= 0);
		checkArgument(deltaBlue >= 0);
		checkArgument(deltaRed+deltaGreen+deltaBlue > 0);
		this.scores = scores;
		this.deltaRed = deltaRed;
		this.deltaGreen = deltaGreen;
		this.deltaBlue = deltaBlue;		
	}
	
	@Override
	public IWVWScores getScores() {
		return this.scores;
	}

	@Override
	public int getDeltaRed() {
		return this.deltaRed;
	}

	@Override
	public int getDeltaGreen() {
		return this.deltaGreen;
	}

	@Override
	public int getDeltaBlue() {
		return this.deltaBlue;
	}

}
