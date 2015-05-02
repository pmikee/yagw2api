package de.justi.yagw2api.wrapper.map;

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

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

import de.justi.yagw2api.arenanet.MapContinentService;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.arenanet.dto.map.MapContinentWithIdDTO;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.domain.map.Continent;
import de.justi.yagw2api.wrapper.domain.map.ContinentMap;

public enum DefaultMapWrapper implements MapWrapper {
	INSTANCE;

	// CONSTS
	private static final MapContinentService SERVICE = YAGW2APIArenanet.getInstance().getMapContinentService();
	private static final Function<MapContinentWithIdDTO, Continent> CONTINENT_CONVERTER = new Function<MapContinentWithIdDTO, Continent>() {

		@Override
		public Continent apply(final MapContinentWithIdDTO input) {
			checkNotNull(input, "missing input");
			return YAGW2APIWrapper.INSTANCE.getMapDomainFactory().newContinentBuilder().id(input.getId()).name(input.getName()).dimension(input.getDimension()).build();
		}
	};

	// EMBEDDED

	// METHODS
	@Override
	public Iterable<Continent> getContinents() {
		return Iterables.transform(SERVICE.retrieveAllContinents(), CONTINENT_CONVERTER);
	}

	@Override
	public ContinentMap getContinentMap(final Continent continent) {
		// FIXME
		return null;
	}

}
