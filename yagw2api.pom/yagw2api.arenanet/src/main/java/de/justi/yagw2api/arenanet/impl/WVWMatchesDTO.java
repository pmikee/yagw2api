package de.justi.yagw2api.arenanet.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
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

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.IWVWMatchDTO;
import de.justi.yagw2api.arenanet.IWVWMatchesDTO;

final class WVWMatchesDTO implements IWVWMatchesDTO {

	@Since(1.0)
	@SerializedName("wvw_matches")
	private WVWMatchDTO[] matches;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("matchesCount", this.matches.length).toString();
	}

	@Override
	public IWVWMatchDTO[] getMatches() {
		return this.matches;
	}
}
