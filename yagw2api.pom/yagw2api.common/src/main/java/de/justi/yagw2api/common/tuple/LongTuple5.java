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

public interface LongTuple5 extends UniformNumberTuple5<Long>, LongTuple4 {
	@Override
	default LongTuple4 asTuple4() {
		return this;
	}

	@Override
	default LongTuple5 multiplyTuple5(final long factor) {
		return Tuples.of(v1Long() * factor, v2Long() * factor, v3Long() * factor, v4Long() * factor, v5Long() * factor);
	}

	@Override
	default LongTuple5 asLongTuple5() {
		return this;
	}

	long v5Long();
}
