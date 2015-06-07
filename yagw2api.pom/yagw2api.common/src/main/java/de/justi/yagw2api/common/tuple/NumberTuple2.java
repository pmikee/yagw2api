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

public interface NumberTuple2<V1 extends Number, V2 extends Number> extends Tuple2<V1, V2>, NumberTuple {

	@Override
	@Nonnull
	V1 v1();

	@Override
	@Nonnull
	V2 v2();

	default int v1Int() {
		return v1().intValue();
	}

	default long v1Long() {
		return v1().longValue();
	}

	default double v1Double() {
		return v1().doubleValue();
	}

	default float v1Float() {
		return v1().floatValue();
	}

	default int v2Int() {
		return v2().intValue();
	}

	default long v2Long() {
		return v2().longValue();
	}

	default double v2Double() {
		return v2().doubleValue();
	}

	default float v2Float() {
		return v2().floatValue();
	}

	default IntTuple2 multiplyTuple2(final int factor) {
		return Tuples.of(v1Int() * factor, v2Int() * factor);
	}

	default IntTuple2 multiplyTuple2(final int factor1, final int factor2) {
		return Tuples.of(v1Int() * factor1, v2Int() * factor2);
	}

	default DoubleTuple2 multiplyTuple2(final double factor) {
		return Tuples.of(v1Double() * factor, v2Double() * factor);
	}

	default DoubleTuple2 multiplyTuple2(final double factor1, final double factor2) {
		return Tuples.of(v1Double() * factor1, v2Double() * factor2);
	}

	default LongTuple2 multiplyTuple2(final long factor) {
		return Tuples.of(v1Long() * factor, v2Long() * factor);
	}

	default LongTuple2 multiplyTuple2(final long factor1, final long factor2) {
		return Tuples.of(v1Long() * factor1, v2Long() * factor2);
	}

	default FloatTuple2 multiplyTuple2(final float factor) {
		return Tuples.of(v1Float() * factor, v2Float() * factor);
	}

	default FloatTuple2 multiplyTuple2(final float factor1, final float factor2) {
		return Tuples.of(v1Float() * factor1, v2Float() * factor2);
	}

	default IntTuple2 asIntTuple2() {
		return Tuples.of(v1Int(), v2Int());
	}

	default DoubleTuple2 asDoubleTuple2() {
		return Tuples.of(v1Double(), v2Double());
	}

	default LongTuple2 asLongTuple2() {
		return Tuples.of(v1Long(), v2Long());
	}

	default FloatTuple2 asFloatTuple2() {
		return Tuples.of(v1Float(), v2Float());
	}
}
