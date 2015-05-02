package de.justi.yagw2api.wrapper.domain.wvw;

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

public enum DefaultWVWDomainFactory implements WVWDomainFactory {
	INSTANCE;

	@Override
	public WVWMap.WVWMapBuilder newMapBuilder() {
		return new DefaultWVWMap.DefaultWVWMapBuilder();
	}

	@Override
	public WVWObjective.WVWObjectiveBuilder newObjectiveBuilder() {
		return new DefaultWVWObjective.DefaultWVWObjectiveBuilder();
	}

	@Override
	public WVWScores newMapScores(final WVWMap map) {
		return new DefaultWVWMapScores(checkNotNull(map));
	}

	@Override
	public WVWMatch.WVWMatchBuilder newMatchBuilder() {
		return new DefaultWVWMatch.DefaultWVWMatchBuilder();
	}

	@Override
	public WVWScores newMatchScores(final WVWMatch match) {
		return new DefaultWVWMatchScores(checkNotNull(match));
	}
}
