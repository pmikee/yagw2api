package de.justi.yagw2api.analyzer.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import static com.google.common.base.Preconditions.checkArgument;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.google.common.base.Objects;

import de.justi.yagw2api.analyzer.IWVWScoresEmbeddable;

@Embeddable
final class WVWScoresEmbeddable implements IWVWScoresEmbeddable {

	public WVWScoresEmbeddable() {
		this(0, 0, 0);
	}

	public WVWScoresEmbeddable(final int redScore, final int greenScore, final int blueScore) {
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

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("red", this.redScores).add("green", this.greenScores).add("blue", this.blueScores).toString();
	}
}
