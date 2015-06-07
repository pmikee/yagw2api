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

public interface NumberTuple2<V1 extends Number, V2 extends Number> extends Tuple2<V1, V2>, NumberTuple {

	default IntTuple2 multiplyTuple2(final int factor) {
		return Tuples.of(v1().intValue() * factor, v2().intValue() * factor);
	}

	default DoubleTuple2 multiplyTuple2(final double factor) {
		return Tuples.of(v1().doubleValue() * factor, v2().doubleValue() * factor);
	}

	default LongTuple2 multiplyTuple2(final long factor) {
		return Tuples.of(v1().longValue() * factor, v2().longValue() * factor);
	}

	default FloatTuple2 multiplyTuple2(final float factor) {
		return Tuples.of(v1().floatValue() * factor, v2().floatValue() * factor);
	}

	default IntTuple2 asIntTuple2() {
		return Tuples.of(this.v1().intValue(), this.v2().intValue());
	}

	default DoubleTuple2 asDoubleTuple2() {
		return Tuples.of(this.v1().doubleValue(), this.v2().doubleValue());
	}

	default LongTuple2 asLongTuple2() {
		return Tuples.of(this.v1().longValue(), this.v2().longValue());
	}

	default FloatTuple2 asFloatTuple2() {
		return Tuples.of(this.v1().floatValue(), this.v2().floatValue());
	}
}
