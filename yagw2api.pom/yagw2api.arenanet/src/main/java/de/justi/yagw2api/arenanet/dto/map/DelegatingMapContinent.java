package de.justi.yagw2api.arenanet.dto.map;

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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

import de.justi.yagw2api.common.tuple.UniformNumberTuple2;

class DelegatingMapContinent implements MapContinentDTO {
	// FIELDS
	private final MapContinentDTO delegate;

	// CONSTRUCTOR
	protected DelegatingMapContinent(final MapContinentDTO delegate) {
		this.delegate = checkNotNull(delegate, "missing delegate");
	}

	// METHODS
	public ToStringHelper toStringHelper() {
		return MoreObjects.toStringHelper(this).add("delegate", this.delegate);
	}

	@Override
	public final String toString() {
		return this.toStringHelper().toString();
	}

	/**
	 * @return
	 * @see de.justi.yagw2api.arenanet.dto.map.MapContinentDTO#getName()
	 */
	@Override
	public String getName() {
		return this.delegate.getName();
	}

	/**
	 * @return
	 * @see de.justi.yagw2api.arenanet.dto.map.MapContinentDTO#getDimension()
	 */
	@Override
	public UniformNumberTuple2<Integer> getDimension() {
		return this.delegate.getDimension();
	}

	/**
	 * @return
	 * @see de.justi.yagw2api.arenanet.dto.map.MapContinentDTO#getMinZoom()
	 */
	@Override
	public int getMinZoom() {
		return this.delegate.getMinZoom();
	}

	/**
	 * @return
	 * @see de.justi.yagw2api.arenanet.dto.map.MapContinentDTO#getMaxZoom()
	 */
	@Override
	public int getMaxZoom() {
		return this.delegate.getMaxZoom();
	}

	/**
	 * @return
	 * @see de.justi.yagw2api.arenanet.dto.map.MapContinentDTO#getFloors()
	 */
	@Override
	public Set<String> getFloors() {
		return this.delegate.getFloors();
	}
}
