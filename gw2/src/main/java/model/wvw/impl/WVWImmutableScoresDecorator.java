package model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.eventbus.EventBus;

import model.wvw.IWVWScores;

class WVWImmutableScoresDecorator implements IWVWScores{

	private final IWVWScores scores;
	
	public WVWImmutableScoresDecorator(IWVWScores scores) {
		this.scores = checkNotNull(scores);
	}
	
	@Override
	public EventBus getChannel() {
		throw new UnsupportedOperationException(this.getClass().getSimpleName()+" is only a decorator for "+AbstractWVWScores.class.getSimpleName()+" and has no channel for its own.");
	}

	@Override
	public int getRedScore() {
		return this.scores.getRedScore();
	}

	@Override
	public int getGreenScore() {
		return this.scores.getGreenScore();
	}

	@Override
	public int getBlueScore() {
		return this.scores.getBlueScore();
	}

	@Override
	public void update(int redScore, int greenScore, int blueScore) {
		throw new UnsupportedOperationException(this.getClass().getSimpleName()+" are immutable and therefore can not be updated.");
	}

	@Override
	public IWVWScores newImmutableReference() {
		return this;
	}

}
