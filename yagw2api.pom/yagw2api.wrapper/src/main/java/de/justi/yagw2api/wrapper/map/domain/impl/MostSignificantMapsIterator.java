package de.justi.yagw2api.wrapper.map.domain.impl;

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

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import de.justi.yagw2api.wrapper.map.domain.Map;
import de.justi.yagwapi.common.tuple.UniformNumberTuple4;

final class MostSignificantMapsIterator implements Iterator<Map> {
	// FIELDS
	private final Iterator<Map> decorated;
	private Optional<Map> next = Optional.absent();
	private final java.util.Map<UniformNumberTuple4<Integer>, Boolean> blockedBounds = Maps.newHashMap();

	// CONSTRUCTOR
	public MostSignificantMapsIterator(final Iterator<Map> decorated) {
		this.decorated = checkNotNull(decorated, "missing decorated");
	}

	// METHODS
	@Override
	public boolean hasNext() {
		return this.peekNext().isPresent();
	}

	private Optional<Map> peekNext() {
		if (!this.next.isPresent()) {
			this.next = this.searchForNextInDecorated();
		}
		return this.next;
	}

	private Optional<Map> pullNext() {
		final Optional<Map> next = this.peekNext();
		this.next = Optional.absent();
		return next;
	}

	private Optional<Map> searchForNextInDecorated() {
		@Nullable
		Map potentialNext = this.decorated.hasNext() ? this.decorated.next() : null;
		while (potentialNext != null) {
			if (this.blockedBounds.containsKey(potentialNext.getBoundsOnContinent())) {
				if (this.decorated.hasNext()) {
					potentialNext = this.decorated.next();
				} else {
					return Optional.absent();
				}
			} else {
				this.blockedBounds.put(potentialNext.getBoundsOnContinent(), true);
				return Optional.of(potentialNext);
			}
		}
		return Optional.absent();
	}

	@Override
	public Map next() {
		final Optional<Map> next = this.pullNext();
		if (next.isPresent()) {
			return next.get();
		} else {
			throw new NoSuchElementException();
		}
	}
}