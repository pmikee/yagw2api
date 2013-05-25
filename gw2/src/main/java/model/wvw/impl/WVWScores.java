package model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Objects;

import model.wvw.IWVWScores;

class WVWScores implements IWVWScores {

	private int redScore = 0;
	private int greenScore = 0;
	private int blueScore = 0;
	
	@Override
	public int getRedScore() {
		return this.redScore;
	}

	@Override
	public void setRedScore(int score) {
		checkArgument(score > 0);
		this.redScore = score;
	}

	@Override
	public int getGreenScore() {
		return this.greenScore;
	}

	@Override
	public void setGreenScore(int score) {
		checkArgument(score > 0);
		this.greenScore = score;
	}

	@Override
	public int getBlueScore() {
		return this.blueScore;
	}

	@Override
	public void setBlueScore(int score) {
		checkArgument(score > 0);
		this.blueScore = score;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("red",this.redScore).add("green", this.greenScore).add("blue", this.blueScore).toString();
	}
}
