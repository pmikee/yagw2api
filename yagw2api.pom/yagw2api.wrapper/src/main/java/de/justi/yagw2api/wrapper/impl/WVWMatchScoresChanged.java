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


import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWScores;

final class WVWMatchScoresChanged extends AbstractWVWScoresChangedEvent implements IWVWMatchScoresChangedEvent {
	private final IWVWMatch match;

	public WVWMatchScoresChanged(IWVWScores scores,int deltaRed, int deltaGreen, int deltaBlue, IWVWMatch match) {
		super(checkNotNull(scores),deltaRed, deltaGreen, deltaBlue);
		this.match = checkNotNull(match);
	}

	@Override
	public IWVWMatch getMatch() {
		return this.match;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("super", super.toString()).add("scores", this.getScores()).add("delta", "r:"+this.getDeltaRed()+",g:"+this.getDeltaGreen()+",b:"+this.getDeltaBlue()).add("matchId", this.getMatch().getId()).toString();
	}
}
