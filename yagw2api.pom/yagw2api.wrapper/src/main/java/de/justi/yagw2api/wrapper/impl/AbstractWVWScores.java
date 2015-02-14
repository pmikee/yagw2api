package de.justi.yagw2api.wrapper.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import static com.google.common.base.Preconditions.checkArgument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.eventbus.EventBus;

import de.justi.yagw2api.wrapper.IWVWScores;
import de.justi.yagwapi.common.AbstractHasChannel;
import de.justi.yagwapi.common.IUnmodifiable;

abstract class AbstractWVWScores extends AbstractHasChannel implements IWVWScores {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWVWScores.class);

	final class UnmodifiableWVWScores implements IWVWScores, IUnmodifiable{
		@Override
		public EventBus getChannel() {
			throw new UnsupportedOperationException(this.getClass().getSimpleName()+" is instance of "+IUnmodifiable.class.getSimpleName()+" and therefore can not be modified.");
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
			throw new UnsupportedOperationException(this.getClass().getSimpleName()+" is instance of "+IUnmodifiable.class.getSimpleName()+" and therefore can not be modified.");
		}

		@Override
		public IWVWScores createUnmodifiableReference() {
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
		return MoreObjects.toStringHelper(this).add("red",this.redScore).add("green", this.greenScore).add("blue", this.blueScore).toString();
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
	public IWVWScores createUnmodifiableReference() {
		return new UnmodifiableWVWScores();
	}
}
