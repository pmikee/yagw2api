package de.justi.yagw2api.core.wrapper.model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.log4j.Logger;

import com.google.common.base.Objects;
import com.google.common.eventbus.EventBus;

import de.justi.yagw2api.core.wrapper.model.AbstractHasChannel;
import de.justi.yagw2api.core.wrapper.model.IImmutable;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWScores;

abstract class AbstractWVWScores extends AbstractHasChannel implements IWVWScores {
	
	private static final Logger LOGGER = Logger.getLogger(AbstractWVWScores.class);

	class WVWImmutableScoresDecorator implements IWVWScores, IImmutable{
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

		public String toString() {
			return Objects.toStringHelper(this).addValue(AbstractWVWScores.this.toString()).toString();
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
		final int deltaRed = redScore - this.redScore;
		final int deltaGreen = greenScore - this.greenScore;
		final int deltaBlue = blueScore - this.blueScore;
		this.redScore = redScore;
		this.greenScore = greenScore;
		this.blueScore = blueScore;
		
		if(changed) {
			LOGGER.trace(this.toString()+" has been changed: deltaRed="+deltaRed+", deltaGreen="+deltaGreen+", deltaBlue="+deltaBlue);
			this.onChange(deltaRed, deltaGreen, deltaBlue);
		}
	}
	
	protected abstract void onChange(int deltaRed, int deltaGreen, int deltaBlue); 

	@Override
	public IWVWScores createImmutableReference() {
		return new WVWImmutableScoresDecorator();
	}
}
