package de.justi.yagw2api.common.tuple;

import javax.annotation.Nonnull;

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

public interface NumberTuple3<V1 extends Number, V2 extends Number, V3 extends Number> extends Tuple3<V1, V2, V3>, NumberTuple2<V1, V2> {

	@Override
	@Nonnull
	V3 v3();

	default int v3Int() {
		return v3().intValue();
	}

	default long v3Long() {
		return v3().longValue();
	}

	default double v3Double() {
		return v3().doubleValue();
	}

	default float v3Float() {
		return v3().floatValue();
	}

	@Override
	default NumberTuple2<V1, V2> asTuple2() {
		return this;
	}

	default IntTuple3 multiplyTuple3(final int factor) {
		return Tuples.of(v1Int() * factor, v2Int() * factor, v3Int() * factor);
	}

	default IntTuple3 multiplyTuple3(final int factor1, final int factor2, final int factor3) {
		return Tuples.of(v1Int() * factor1, v2Int() * factor2, v3Int() * factor3);
	}

	default DoubleTuple3 multiplyTuple3(final double factor) {
		return Tuples.of(v1Double() * factor, v2Double() * factor, v3Double() * factor);
	}

	default DoubleTuple3 multiplyTuple3(final double factor1, final double factor2, final double factor3) {
		return Tuples.of(v1Double() * factor1, v2Double() * factor2, v3Double() * factor3);
	}

	default LongTuple3 multiplyTuple3(final long factor) {
		return Tuples.of(v1Long() * factor, v2Long() * factor, v3Long() * factor);
	}

	default LongTuple3 multiplyTuple3(final long factor1, final long factor2, final long factor3) {
		return Tuples.of(v1Long() * factor1, v2Long() * factor2, v3Long() * factor3);
	}

	default FloatTuple3 multiplyTuple3(final float factor) {
		return Tuples.of(v1Float() * factor, v2Float() * factor, v3Float() * factor);
	}

	default FloatTuple3 multiplyTuple3(final float factor1, final float factor2, final float factor3) {
		return Tuples.of(v1Float() * factor1, v2Float() * factor2, v3Float() * factor3);
	}

	default IntTuple3 asIntTuple3() {
		return Tuples.of(v1Int(), v2Int(), v3Int());
	}

	default DoubleTuple3 asDoubleTuple3() {
		return Tuples.of(v1Double(), v2Double(), v3Double());
	}

	default LongTuple3 asLongTuple3() {
		return Tuples.of(v1Long(), v2Long(), v3Long());
	}

	default FloatTuple3 asFloatTuple3() {
		return Tuples.of(v1Float(), v2Float(), v3Float());
	}

}
