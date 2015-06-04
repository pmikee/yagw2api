package de.justi.yagwapi.common.tuple;

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

import javax.annotation.Nullable;

final class DefaultUniformNullSupportingTuple5<V> extends AbstractTuple5<V, V, V, V, V> implements UniformTuple5<V> {

	DefaultUniformNullSupportingTuple5(@Nullable final V value1, @Nullable final V value2, @Nullable final V value3, @Nullable final V value4, @Nullable final V value5) {
		super(value1, value2, value3, value4, value5);
	}

}
