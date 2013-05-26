package model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import model.AbstractHasChannel;
import model.wvw.IWVWScores;

import com.google.common.base.Objects;

class WVWScores extends AbstractHasChannel implements IWVWScores {

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
			//TODO create event
		}
	}

	@Override
	public IWVWScores newImmutableReference() {
		return new WVWImmutableScoresDecorator(this);
	}
}
