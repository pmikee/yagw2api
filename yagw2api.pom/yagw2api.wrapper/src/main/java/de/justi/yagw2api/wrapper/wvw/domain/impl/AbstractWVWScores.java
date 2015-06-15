package de.justi.yagw2api.wrapper.wvw.domain.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.eventbus.EventBus;

import de.justi.yagw2api.common.event.AbstractHasChannel;
import de.justi.yagw2api.wrapper.wvw.domain.WVWScores;

abstract class AbstractWVWScores extends AbstractHasChannel implements WVWScores {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWVWScores.class);

	final class UnmodifiableWVWScores implements WVWScores {
		@Override
		public EventBus getChannel() {
			throw new UnsupportedOperationException("unmodifiable");
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
		public void update(final int redScore, final int greenScore, final int blueScore) {
			throw new UnsupportedOperationException("unmodifiable");
		}

		@Override
		public WVWScores createUnmodifiableReference() {
			return this;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).addValue(AbstractWVWScores.this.toString()).toString();
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

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("red", this.redScore).add("green", this.greenScore).add("blue", this.blueScore).toString();
	}

	@Override
	public void update(final int redScore, final int greenScore, final int blueScore) {
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

		if (changed) {
			LOGGER.trace(this.toString() + " has been changed: deltaRed=" + deltaRed + ", deltaGreen=" + deltaGreen + ", deltaBlue=" + deltaBlue);
			this.onChange(deltaRed, deltaGreen, deltaBlue);
		}
	}

	protected abstract void onChange(int deltaRed, int deltaGreen, int deltaBlue);

	@Override
	public WVWScores createUnmodifiableReference() {
		return new UnmodifiableWVWScores();
	}
}
