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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

public final class Tuples {
	// STATIC METHODS

	// > MERGE
	public static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> merge(final Tuple2<V1, V2> part1, final Tuple2<V3, V4> part2) {
		checkNotNull(part1, "missing part1");
		checkNotNull(part2, "missing part2");
		return Tuples.of(part1.v1(), part1.v2(), part2.v1(), part2.v2());
	}

	public static <V1 extends Number, V2 extends Number, V3 extends Number, V4 extends Number> NumberTuple4<V1, V2, V3, V4> merge(final NumberTuple2<V1, V2> part1,
			final NumberTuple2<V3, V4> part2) {
		checkNotNull(part1, "missing part1");
		checkNotNull(part2, "missing part2");
		return Tuples.of(part1.v1(), part1.v2(), part2.v1(), part2.v2());
	}

	public static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> merge(@Nullable final V1 v1, @Nullable final V2 v2, final Tuple2<V3, V4> part2) {
		checkNotNull(part2, "missing part2");
		return Tuples.of(v1, v2, part2.v1(), part2.v2());
	}

	public static <V1 extends Number, V2 extends Number, V3 extends Number, V4 extends Number> NumberTuple4<V1, V2, V3, V4> merge(@Nullable final V1 v1, @Nullable final V2 v2,
			final Tuple2<V3, V4> part2) {
		checkNotNull(v1, "missing v1");
		checkNotNull(v2, "missing v2");
		checkNotNull(part2, "missing part2");
		return Tuples.of(v1, v2, part2.v1(), part2.v2());
	}

	public static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> merge(final Tuple2<V1, V2> part1, @Nullable final V3 v3, @Nullable final V4 v4) {
		checkNotNull(part1, "missing part1");
		return Tuples.of(part1.v1(), part1.v2(), v3, v4);
	}

	public static <V1 extends Number, V2 extends Number, V3 extends Number, V4 extends Number> NumberTuple4<V1, V2, V3, V4> merge(final Tuple2<V1, V2> part1, final V3 v3,
			final V4 v4) {
		checkNotNull(part1, "missing part1");
		checkNotNull(v3, "missing v3");
		checkNotNull(v4, "missing v4");
		return Tuples.of(part1.v1(), part1.v2(), v3, v4);
	}

	// > NUMBER TUPLES
	public static <V1 extends Number, V2 extends Number> NumberTuple2<V1, V2> of(final V1 v1, final V2 v2) {
		return new DefaultNumberTuple2<V1, V2>(checkNotNull(v1, "missing v1"), checkNotNull(v2, "missing v2"));
	}

	public static <V1 extends Number, V2 extends Number, V3 extends Number> NumberTuple3<V1, V2, V3> of(final V1 v1, final V2 v2, final V3 v3) {
		return new DefaultNumberTuple3<V1, V2, V3>(checkNotNull(v1, "missing v1"), checkNotNull(v2, "missing v2"), checkNotNull(v3, "missing v3"));
	}

	public static <V1 extends Number, V2 extends Number, V3 extends Number, V4 extends Number> NumberTuple4<V1, V2, V3, V4> of(final V1 v1, final V2 v2, final V3 v3, final V4 v4) {
		return new DefaultNumberTuple4<V1, V2, V3, V4>(checkNotNull(v1, "missing v1"), checkNotNull(v2, "missing v2"), checkNotNull(v3, "missing v3"), checkNotNull(v4,
				"missing v4"));
	}

	public static <V1 extends Number, V2 extends Number, V3 extends Number, V4 extends Number, V5 extends Number> NumberTuple5<V1, V2, V3, V4, V5> of(final V1 v1, final V2 v2,
			final V3 v3, final V4 v4, final V5 v5) {
		return new DefaultNumberTuple5<V1, V2, V3, V4, V5>(checkNotNull(v1, "missing v1"), checkNotNull(v2, "missing v2"), checkNotNull(v3, "missing v3"), checkNotNull(v4,
				"missing v4"), checkNotNull(v5, "missing v5"));
	}

	// > NULLSUPPORTING TUPLES

	public static <V1, V2> Tuple2<V1, V2> of(@Nullable final V1 v1, @Nullable final V2 v2) {
		return new DefaultNullSupportingTuple2<V1, V2>(v1, v2);
	}

	public static <V1, V2, V3> Tuple3<V1, V2, V3> of(@Nullable final V1 v1, @Nullable final V2 v2, @Nullable final V3 v3) {
		return new DefaultNullSupportingTuple3<V1, V2, V3>(v1, v2, v3);
	}

	public static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> of(@Nullable final V1 v1, @Nullable final V2 v2, @Nullable final V3 v3, @Nullable final V4 v4) {
		return new DefaultNullSupportingTuple4<V1, V2, V3, V4>(v1, v2, v3, v4);
	}

	public static <V1, V2, V3, V4, V5> Tuple5<V1, V2, V3, V4, V5> of(@Nullable final V1 v1, @Nullable final V2 v2, @Nullable final V3 v3, @Nullable final V4 v4,
			@Nullable final V5 v5) {
		return new DefaultNullSupportingTuple5<V1, V2, V3, V4, V5>(v1, v2, v3, v4, v5);
	}

	// > UTILITIES

	/**
	 * Two rectangles do not overlap if one of the following conditions is true:
	 * <ol>
	 * <li>One rectangle is above top edge of other rectangle.</li>
	 * <li>One rectangle is on left side of left edge of other rectangle.</li>
	 * </ol>
	 *
	 * @param left
	 * @param right
	 * @return
	 */
	public static boolean overlaps(final NumberTuple4<?, ?, ?, ?> left, final NumberTuple4<?, ?, ?, ?> right) {

		// If one rectangle is on left side of other
		if (left.v1().doubleValue() > right.v3().doubleValue() || right.v1().doubleValue() > left.v3().doubleValue()) {
			return false;
		}

		// If one rectangle is above other
		if (left.v2().doubleValue() > right.v4().doubleValue() || right.v2().doubleValue() > left.v4().doubleValue()) {
			return false;
		}

		return true;
	}

	// CONSTRUCTORS
	private Tuples() {
		throw new AssertionError("no instance");
	}
}