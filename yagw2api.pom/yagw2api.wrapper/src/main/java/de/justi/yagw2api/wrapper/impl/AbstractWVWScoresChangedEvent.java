package de.justi.yagw2api.wrapper.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
 * -------------------------------------------------------------
 * Copyright (C) 2012 - 2013 Julian Stitz
 * -------------------------------------------------------------
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
import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.yagw2api.wrapper.IWVWScores;
import de.justi.yagw2api.wrapper.IWVWScoresChangedEvent;
import de.justi.yagwapi.common.AbstractEvent;

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
