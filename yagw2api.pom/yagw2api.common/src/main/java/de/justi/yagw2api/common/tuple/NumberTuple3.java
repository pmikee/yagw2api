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

public interface NumberTuple3<V1 extends Number, V2 extends Number, V3 extends Number> extends Tuple3<V1, V2, V3>, NumberTuple2<V1, V2> {

	@Override
	default NumberTuple2<V1, V2> asTuple2() {
		return this;
	}

	default IntTuple3 multiplyTuple3(final int factor) {
		return Tuples.of(v1().intValue() * factor, v2().intValue() * factor, v3().intValue() * factor);
	}

	default DoubleTuple3 multiplyTuple3(final double factor) {
		return Tuples.of(v1().doubleValue() * factor, v2().doubleValue() * factor, v3().doubleValue() * factor);
	}

	default LongTuple3 multiplyTuple3(final long factor) {
		return Tuples.of(v1().longValue() * factor, v2().longValue() * factor, v3().longValue() * factor);
	}

	default FloatTuple3 multiplyTuple3(final float factor) {
		return Tuples.of(v1().floatValue() * factor, v2().floatValue() * factor, v3().floatValue() * factor);
	}

	default IntTuple3 asIntTuple3() {
		return Tuples.of(this.v1().intValue(), this.v2().intValue(), this.v3().intValue());
	}

	default DoubleTuple3 asDoubleTuple3() {
		return Tuples.of(this.v1().doubleValue(), this.v2().doubleValue(), this.v3().doubleValue());
	}

	default LongTuple3 asLongTuple3() {
		return Tuples.of(this.v1().longValue(), this.v2().longValue(), this.v3().longValue());
	}

	default FloatTuple3 asFloatTuple3() {
		return Tuples.of(this.v1().floatValue(), this.v2().floatValue(), this.v3().floatValue());
	}

}
