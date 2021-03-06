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

public interface IntTuple3 extends UniformNumberTuple3<Integer>, IntTuple2 {
	default IntTuple3 clampTuple3(final int min, final int max) {
		return Tuples.of(Math.clamp(v1Int(), min, max), Math.clamp(v2Int(), min, max), Math.clamp(v3Int(), min, max));
	}

	@Override
	default IntTuple2 asTuple2() {
		return this;
	}

	@Override
	default IntTuple3 asIntTuple3() {
		return this;
	}

}
