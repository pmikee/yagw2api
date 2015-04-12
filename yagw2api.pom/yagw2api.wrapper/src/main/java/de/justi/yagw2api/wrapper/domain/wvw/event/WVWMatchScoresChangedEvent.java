package de.justi.yagw2api.wrapper.domain.wvw.event;

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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;

import de.justi.yagw2api.wrapper.domain.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.domain.wvw.IWVWScores;

final class WVWMatchScoresChangedEvent extends AbstractWVWScoresChangedEvent implements IWVWMatchScoresChangedEvent {
	private final IWVWMatch match;

	public WVWMatchScoresChangedEvent(final IWVWScores scores, final int deltaRed, final int deltaGreen, final int deltaBlue, final IWVWMatch match) {
		super(checkNotNull(scores), deltaRed, deltaGreen, deltaBlue);
		this.match = checkNotNull(match);
	}

	@Override
	public IWVWMatch getMatch() {
		return this.match;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("super", super.toString()).add("scores", this.getScores())
				.add("delta", "r:" + this.getDeltaRed() + ",g:" + this.getDeltaGreen() + ",b:" + this.getDeltaBlue()).add("matchId", this.getMatch().getId()).toString();
	}
}
