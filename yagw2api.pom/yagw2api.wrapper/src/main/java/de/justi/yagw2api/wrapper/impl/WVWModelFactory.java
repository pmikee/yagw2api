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
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWModelFactory;
import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWVWScores;

final class WVWModelFactory implements IWVWModelFactory {

	@Override
	public IWVWMap.IWVWMapBuilder newMapBuilder() {
		return new WVWMap.WVWMapBuilder();
	}

	@Override
	public IWVWObjective.IWVWObjectiveBuilder newObjectiveBuilder() {
		return new WVWObjective.WVWObjectiveBuilder();
	}

	@Override
	public IWVWScores newMapScores(IWVWMap map) {
		return new WVWMapScores(checkNotNull(map));
	}

	@Override
	public IWVWMatch.IWVWMatchBuilder newMatchBuilder() {
		return new WVWMatch.WVWMatchBuilder();
	}

	@Override
	public IWVWScores newMatchScores(IWVWMatch match) {
		return new WVWMatchScores(checkNotNull(match));
	}
}