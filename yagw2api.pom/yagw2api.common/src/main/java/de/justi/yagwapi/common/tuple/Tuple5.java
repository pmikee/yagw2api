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

import com.google.common.base.Optional;

public interface Tuple5<V1, V2, V3, V4, V5> extends Tuple<V1, V5> {

	V1 v1();

	V2 v2();

	V3 v3();

	V4 v4();

	V5 v5();

	Optional<V1> getValue1();

	Tuple5<V1, V2, V3, V4, V5> setValue1(final V1 value);

	Optional<V2> getValue2();

	Tuple5<V1, V2, V3, V4, V5> setValue2(final V2 value);

	Optional<V3> getValue3();

	Tuple5<V1, V2, V3, V4, V5> setValue3(final V3 value);

	Optional<V4> getValue4();

	Tuple5<V1, V2, V3, V4, V5> setValue4(final V4 value);

	Optional<V5> getValue5();

	Tuple5<V1, V2, V3, V4, V5> setValue5(final V5 value);
}
