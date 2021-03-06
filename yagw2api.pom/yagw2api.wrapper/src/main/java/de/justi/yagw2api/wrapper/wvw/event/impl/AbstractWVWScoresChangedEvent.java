package de.justi.yagw2api.wrapper.wvw.event.impl;

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
import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.yagw2api.common.event.AbstractEvent;
import de.justi.yagw2api.wrapper.wvw.domain.WVWScores;
import de.justi.yagw2api.wrapper.wvw.event.WVWScoresChangedEvent;

abstract class AbstractWVWScoresChangedEvent extends AbstractEvent implements WVWScoresChangedEvent {
	private final WVWScores scores;
	private final int deltaRed;
	private final int deltaGreen;
	private final int deltaBlue;

	public AbstractWVWScoresChangedEvent(final WVWScores scores, final int deltaRed, final int deltaGreen, final int deltaBlue) {
		super();
		checkNotNull(scores);
		checkArgument(deltaRed >= 0);
		checkArgument(deltaGreen >= 0);
		checkArgument(deltaBlue >= 0);
		checkArgument(deltaRed + deltaGreen + deltaBlue > 0);
		this.scores = scores;
		this.deltaRed = deltaRed;
		this.deltaGreen = deltaGreen;
		this.deltaBlue = deltaBlue;
	}

	@Override
	public WVWScores getScores() {
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
