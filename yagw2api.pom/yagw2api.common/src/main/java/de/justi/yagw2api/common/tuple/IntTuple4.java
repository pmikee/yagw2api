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

public interface IntTuple4 extends UniformNumberTuple4<Integer>, IntTuple3 {

	default IntTuple4 clampTuple4(final int min, final int max) {
		return Tuples.of(Math.clamp(v1Int(), min, max), Math.clamp(v2Int(), min, max), Math.clamp(v3Int(), min, max), Math.clamp(v4Int(), min, max));
	}

	@Override
	default IntTuple3 asTuple3() {
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
	default boolean overlaps(final NumberTuple4<Integer, Integer, Integer, Integer> right) {
		// If one rectangle is on left side of other
		if (v1Int() > right.v3().intValue() || right.v1().intValue() > v3Int()) {
			return false;
		}

		// If one rectangle is above other
		if (v2Int() > right.v4().intValue() || right.v2().intValue() > v4Int()) {
			return false;
		}
		return true;
	}

	@Override
	default IntTuple4 asIntTuple4() {
		return this;
	}

}
