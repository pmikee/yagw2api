package de.justi.yagw2api.wrapper.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Objects.ToStringHelper;

import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWScores;

final class WVWMapScoresChangedEvent extends AbstractWVWScoresChangedEvent implements IWVWMapScoresChangedEvent {
	private final IWVWMap map;
	
	public WVWMapScoresChangedEvent(IWVWScores scores,int deltaRed, int deltaGreen, int deltaBlue, IWVWMap map) {
		super(checkNotNull(scores), deltaRed, deltaGreen, deltaBlue);
		this.map = checkNotNull(map);
	}

	@Override
	public IWVWMap getMap() {
		return this.map;
	}

	public String toString() {
		final Optional<IWVWMatch> matchOptional = this.getMap().getMatch();
		final ToStringHelper helper = Objects.toStringHelper(this).add("super", super.toString()).add("scores", this.getScores()).add("delta", "r:"+this.getDeltaRed()+",g:"+this.getDeltaGreen()+",b:"+this.getDeltaBlue()).add("mapType",this.getMap().getType());
		if(matchOptional.isPresent()) {
			helper.add("matchId", matchOptional.get().getId());
		}
		return helper.toString();
	}
}
