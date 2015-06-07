package de.justi.yagw2api.common.tuple;

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

public interface FloatTuple5 extends UniformNumberTuple5<Float>, FloatTuple4 {
	@Override
	default FloatTuple4 asTuple4() {
		return this;
	}

	@Override
	default FloatTuple5 multiplyTuple5(final float factor) {
		return Tuples.of(v1Float() * factor, v2Float() * factor, v3Float() * factor, v4Float() * factor, v5Float() * factor);
	}

	@Override
	default FloatTuple5 asFloatTuple5() {
		return this;
	}

	float v5Float();
}
