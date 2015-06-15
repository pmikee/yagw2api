package de.justi.yagw2api.common.tuple;

import de.justi.yagw2api.common.math.Math;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Commons
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

public interface LongTuple4 extends UniformNumberTuple4<Long>, LongTuple3 {
	default LongTuple4 clampTuple4(final long min, final long max) {
		return Tuples.of(Math.clamp(v1Long(), min, max), Math.clamp(v2Long(), min, max), Math.clamp(v3Long(), min, max), Math.clamp(v4Long(), min, max));
	}

	@Override
	default LongTuple3 asTuple3() {
		return this;
	}

	/**
	 * Two rectangles do not overlap if one of the following conditions is true:
	 * <ol>
	 * <li>One rectangle is above top edge of other rectangle.</li>
	 * <li>One rectangle is on left side of left edge of other rectangle.</li>
	 * </ol>
	 *
	 * @param right
	 * @return
	 */
	default boolean overlaps(final NumberTuple4<Long, Long, Long, Long> right) {
		// If one rectangle is on left side of other
		if (v1Long() > right.v3().longValue() || right.v1().longValue() > v3Long()) {
			return false;
		}

		// If one rectangle is above other
		if (v2Long() > right.v4().longValue() || right.v2().longValue() > v4Long()) {
			return false;
		}
		return true;
	}

	@Override
	default LongTuple4 asLongTuple4() {
		return this;
	}
}
