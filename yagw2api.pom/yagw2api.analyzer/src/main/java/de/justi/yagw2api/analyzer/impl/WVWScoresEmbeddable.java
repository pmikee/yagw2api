package de.justi.yagw2api.analyzer.impl;

import static com.google.common.base.Preconditions.checkArgument;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.google.common.base.Objects;

import de.justi.yagw2api.analyzer.IWVWScoresEmbeddable;

@Embeddable
final class WVWScoresEmbeddable implements IWVWScoresEmbeddable {

	public WVWScoresEmbeddable() {
		this(0,0,0);
	}
	
	public WVWScoresEmbeddable(int redScore, int greenScore, int blueScore) {
		checkArgument(redScore >= 0);
		checkArgument(greenScore >= 0);
		checkArgument(blueScore >= 0);
		this.redScores = redScore;
		this.greenScores = greenScore;
		this.blueScores = blueScore;
	}
	
	@Column(name = "red_scores")
	private int redScores;
	
	@Column(name = "green_scores")
	private int greenScores;
	
	@Column(name = "blue_scores")
	private int blueScores;
	
	@Override
	public int getRedScore() {
		return this.redScores;
	}

	@Override
	public int getBlueScore() {
		return this.blueScores;
	}

	@Override
	public int getGreenScore() {
		return this.greenScores;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("red",this.redScores).add("green", this.greenScores).add("blue", this.blueScores).toString();
	}
}
