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

public interface NumberTuple4<V1 extends Number, V2 extends Number, V3 extends Number, V4 extends Number> extends Tuple4<V1, V2, V3, V4>, NumberTuple3<V1, V2, V3> {

	@Override
	default NumberTuple3<V1, V2, V3> asTuple3() {
		return this;
	}

	default IntTuple4 multiplyTuple4(final int factor) {
		return Tuples.of(v1().intValue() * factor, v2().intValue() * factor, v3().intValue() * factor, v4().intValue() * factor);
	}

	default DoubleTuple4 multiplyTuple4(final double factor) {
		return Tuples.of(v1().doubleValue() * factor, v2().doubleValue() * factor, v3().doubleValue() * factor, v4().doubleValue() * factor);
	}

	default LongTuple4 multiplyTuple4(final long factor) {
		return Tuples.of(v1().longValue() * factor, v2().longValue() * factor, v3().longValue() * factor, v4().longValue() * factor);
	}

	default FloatTuple4 multiplyTuple4(final float factor) {
		return Tuples.of(v1().floatValue() * factor, v2().floatValue() * factor, v3().floatValue() * factor, v4().floatValue() * factor);
	}

	default IntTuple4 asIntTuple4() {
		return Tuples.of(this.v1().intValue(), this.v2().intValue(), this.v3().intValue(), this.v4().intValue());
	}

	default DoubleTuple4 asDoubleTuple4() {
		return Tuples.of(this.v1().doubleValue(), this.v2().doubleValue(), this.v3().doubleValue(), this.v4().doubleValue());
	}

	default LongTuple4 asLongTuple4() {
		return Tuples.of(this.v1().longValue(), this.v2().longValue(), this.v3().longValue(), this.v4().longValue());
	}

	default FloatTuple4 asFloatTuple4() {
		return Tuples.of(this.v1().floatValue(), this.v2().floatValue(), this.v3().floatValue(), this.v4().floatValue());
	}
}
