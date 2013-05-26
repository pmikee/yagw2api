package model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import model.AbstractHasChannel;
import model.wvw.IWVWScores;

import com.google.common.base.Objects;
import com.google.common.eventbus.EventBus;

abstract class AbstractWVWScores extends AbstractHasChannel implements IWVWScores {

	class WVWImmutableScoresDecorator implements IWVWScores{
		@Override
		public EventBus getChannel() {
			throw new UnsupportedOperationException(this.getClass().getSimpleName()+" is only a decorator for "+AbstractWVWScores.class.getSimpleName()+" and has no channel for its own.");
		}

		@Override
		public int getRedScore() {
			return AbstractWVWScores.this.getRedScore();
		}

		@Override
		public int getGreenScore() {
			return AbstractWVWScores.this.getGreenScore();
		}

		@Override
		public int getBlueScore() {
			return AbstractWVWScores.this.getBlueScore();
		}

		@Override
		public void update(int redScore, int greenScore, int blueScore) {
			throw new UnsupportedOperationException(this.getClass().getSimpleName()+" are immutable and therefore can not be updated.");
		}

		@Override
		public IWVWScores createImmutableReference() {
			return this;
		}

	}
	
	// FIELDS
	private int redScore = 0;
	private int greenScore = 0;
	private int blueScore = 0;
	
	@Override
	public int getRedScore() {
		return this.redScore;
	}

	@Override
	public int getGreenScore() {
		return this.greenScore;
	}

	@Override
	public int getBlueScore() {
		return this.blueScore;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("red",this.redScore).add("green", this.greenScore).add("blue", this.blueScore).toString();
	}

	@Override
	public void update(int redScore, int greenScore, int blueScore) {
		checkArgument(redScore >= 0);
		checkArgument(greenScore >= 0);
		checkArgument(blueScore >= 0);
		boolean changed = this.redScore != redScore || this.greenScore != greenScore || this.blueScore != blueScore;
		this.redScore = redScore;
		this.greenScore = greenScore;
		this.blueScore = blueScore;
		
		if(changed) {
			this.onChange();
		}
	}
	
	protected abstract void onChange(); 

	@Override
	public IWVWScores createImmutableReference() {
		return new WVWImmutableScoresDecorator();
	}
}
