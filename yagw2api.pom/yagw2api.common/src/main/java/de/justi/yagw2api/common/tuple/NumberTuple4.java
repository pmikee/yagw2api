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

public interface NumberTuple4<V1 extends Number, V2 extends Number, V3 extends Number, V4 extends Number> extends Tuple4<V1, V2, V3, V4>, NumberTuple3<V1, V2, V3> {

	@Override
	@Nonnull
	V4 v4();

	default int v4Int() {
		return v4().intValue();
	}

	default long v4Long() {
		return v4().longValue();
	}

	default double v4Double() {
		return v4().doubleValue();
	}

	default float v4Float() {
		return v4().floatValue();
	}

	@Override
	default NumberTuple3<V1, V2, V3> asTuple3() {
		return this;
	}

	default IntTuple4 multiplyTuple4(final int factor) {
		return Tuples.of(v1Int() * factor, v2Int() * factor, v3Int() * factor, v4Int() * factor);
	}

	default IntTuple4 multiplyTuple4(final int factor1, final int factor2, final int factor3, final int factor4) {
		return Tuples.of(v1Int() * factor1, v2Int() * factor2, v3Int() * factor3, v4Int() * factor4);
	}

	default DoubleTuple4 multiplyTuple4(final double factor) {
		return Tuples.of(v1Double() * factor, v2Double() * factor, v3Double() * factor, v4Double() * factor);
	}

	default DoubleTuple4 multiplyTuple4(final double factor1, final double factor2, final double factor3, final double factor4) {
		return Tuples.of(v1Double() * factor1, v2Double() * factor2, v3Double() * factor3, v4Double() * factor4);
	}

	default LongTuple4 multiplyTuple4(final long factor) {
		return Tuples.of(v1Long() * factor, v2Long() * factor, v3Long() * factor, v4Long() * factor);
	}

	default LongTuple4 multiplyTuple4(final long factor1, final long factor2, final long factor3, final long factor4) {
		return Tuples.of(v1Long() * factor1, v2Long() * factor2, v3Long() * factor3, v4Long() * factor4);
	}

	default FloatTuple4 multiplyTuple4(final float factor) {
		return Tuples.of(v1Float() * factor, v2Float() * factor, v3Float() * factor, v4Float() * factor);
	}

	default FloatTuple4 multiplyTuple4(final float factor1, final float factor2, final float factor3, final float factor4) {
		return Tuples.of(v1Float() * factor1, v2Float() * factor2, v3Float() * factor3, v4Float() * factor4);
	}

	default IntTuple4 asIntTuple4() {
		return Tuples.of(v1Int(), v2Int(), v3Int(), v4Int());
	}

	default DoubleTuple4 asDoubleTuple4() {
		return Tuples.of(v1Double(), v2Double(), v3Double(), v4Double());
	}

	default LongTuple4 asLongTuple4() {
		return Tuples.of(v1Long(), v2Long(), v3Long(), v4Long());
	}

	default FloatTuple4 asFloatTuple4() {
		return Tuples.of(v1Float(), v2Float(), v3Float(), v4Float());
	}
}
