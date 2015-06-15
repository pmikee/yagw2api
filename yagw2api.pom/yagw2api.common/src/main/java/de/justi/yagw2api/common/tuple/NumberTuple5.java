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

public interface NumberTuple5<V1 extends Number, V2 extends Number, V3 extends Number, V4 extends Number, V5 extends Number> extends Tuple5<V1, V2, V3, V4, V5>,
		NumberTuple4<V1, V2, V3, V4> {

	@Override
	@Nonnull
	V5 v5();

	default int v5Int() {
		return v5().intValue();
	}

	default long v5Long() {
		return v5().longValue();
	}

	default double v5Double() {
		return v5().doubleValue();
	}

	default float v5Float() {
		return v5().floatValue();
	}

	@Override
	default NumberTuple4<V1, V2, V3, V4> asTuple4() {
		return this;
	}

	default IntTuple5 multiplyTuple5(final int factor) {
		return Tuples.of(v1Int() * factor, v2Int() * factor, v3Int() * factor, v4().intValue() * factor, v5Int() * factor);
	}

	default IntTuple5 multiplyTuple5(final int factor1, final int factor2, final int factor3, final int factor4, final int factor5) {
		return Tuples.of(v1Int() * factor1, v2Int() * factor2, v3Int() * factor3, v4().intValue() * factor4, v5Int() * factor5);
	}

	default DoubleTuple5 multiplyTuple5(final double factor) {
		return Tuples.of(v1Double() * factor, v2Double() * factor, v3Double() * factor, v4Double() * factor, v5Double() * factor);
	}

	default DoubleTuple5 multiplyTuple5(final double factor1, final double factor2, final double factor3, final double factor4, final double factor5) {
		return Tuples.of(v1Double() * factor1, v2Double() * factor2, v3Double() * factor3, v4Double() * factor4, v5Double() * factor5);
	}

	default LongTuple5 multiplyTuple5(final long factor) {
		return Tuples.of(v1Long() * factor, v2Long() * factor, v3Long() * factor, v4Long() * factor, v5Long() * factor);
	}

	default LongTuple5 multiplyTuple5(final long factor1, final long factor2, final long factor3, final long factor4, final long factor5) {
		return Tuples.of(v1Long() * factor1, v2Long() * factor2, v3Long() * factor3, v4Long() * factor4, v5Long() * factor5);
	}

	default FloatTuple5 multiplyTuple5(final float factor) {
		return Tuples.of(v1Float() * factor, v2Float() * factor, v3Float() * factor, v4Float() * factor, v5Float() * factor);
	}

	default FloatTuple5 multiplyTuple5(final float factor1, final float factor2, final float factor3, final float factor4, final float factor5) {
		return Tuples.of(v1Float() * factor1, v2Float() * factor2, v3Float() * factor3, v4Float() * factor4, v5Float() * factor5);
	}

	default IntTuple5 asIntTuple5() {
		return Tuples.of(v1Int(), v2Int(), v3Int(), v4Int(), v5Int());
	}

	default DoubleTuple5 asDoubleTuple5() {
		return Tuples.of(v1Double(), v2Double(), v3Double(), v4Double(), v5Double());
	}

	default LongTuple5 asLongTuple5() {
		return Tuples.of(v1Long(), v2Long(), v3Long(), v4Long(), v5Long());
	}

	default FloatTuple5 asFloatTuple5() {
		return Tuples.of(v1Float(), v2Float(), v3Float(), v4Float(), v5Float());
	}
}
