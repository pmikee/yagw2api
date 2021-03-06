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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.wvw.domain.WVWMap;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMatch;
import de.justi.yagw2api.wrapper.wvw.domain.WVWScores;
import de.justi.yagw2api.wrapper.wvw.event.WVWMapScoresChangedEvent;

final class DefaultWVWMapScoresChangedEvent extends AbstractWVWScoresChangedEvent implements WVWMapScoresChangedEvent {
	private final WVWMap map;

	public DefaultWVWMapScoresChangedEvent(final WVWScores scores, final int deltaRed, final int deltaGreen, final int deltaBlue, final WVWMap map) {
		super(checkNotNull(scores), deltaRed, deltaGreen, deltaBlue);
		this.map = checkNotNull(map);
	}

	@Override
	public WVWMap getMap() {
		return this.map;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		final Optional<WVWMatch> matchOptional = this.getMap().getMatch();
		final com.google.common.base.MoreObjects.ToStringHelper helper = super.toStringHelper().add("scores", this.getScores())
				.add("delta", "r:" + this.getDeltaRed() + ",g:" + this.getDeltaGreen() + ",b:" + this.getDeltaBlue()).add("mapType", this.getMap().getType());
		if (matchOptional.isPresent()) {
			helper.add("matchId", matchOptional.get().getId());
		}
		return helper;
	}
}
