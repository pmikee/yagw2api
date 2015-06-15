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

public interface DoubleTuple5 extends UniformNumberTuple5<Double>, DoubleTuple4 {
	default DoubleTuple5 clampTuple5(final double min, final double max) {
		return Tuples.of(Math.clamp(v1Double(), min, max), Math.clamp(v2Double(), min, max), Math.clamp(v3Double(), min, max), Math.clamp(v4Double(), min, max),
				Math.clamp(v5Double(), min, max));
	}

	@Override
	default DoubleTuple4 asTuple4() {
		return this;
	}

	@Override
	default DoubleTuple5 asDoubleTuple5() {
		return this;
	}
}
