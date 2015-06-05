package de.justi.yagw2api.common.tuple;

import javax.annotation.Nullable;

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

final class DefaultNullSupportingTuple5<V1, V2, V3, V4, V5> extends AbstractTuple5<V1, V2, V3, V4, V5> implements Tuple5<V1, V2, V3, V4, V5> {

	DefaultNullSupportingTuple5(@Nullable final V1 value1, @Nullable final V2 value2, @Nullable final V3 value3, @Nullable final V4 value4, @Nullable final V5 value5) {
		super(value1, value2, value3, value4, value5);
	}

}
