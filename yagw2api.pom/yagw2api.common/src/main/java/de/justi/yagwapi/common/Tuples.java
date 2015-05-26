package de.justi.yagwapi.common;

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

	public static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> merge(final Tuple2<V1, V2> part1, final Tuple2<V3, V4> part2) {
		checkNotNull(part1, "missing part1");
		checkNotNull(part2, "missing part2");
		return Tuples.of(part1.v1(), part1.v2(), part2.v1(), part2.v2());
	}

	public static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> merge(@Nullable final V1 v1, @Nullable final V2 v2, final Tuple2<V3, V4> part2) {
		checkNotNull(part2, "missing part2");
		return Tuples.of(v1, v2, part2.v1(), part2.v2());
	}

	public static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> merge(final Tuple2<V1, V2> part1, @Nullable final V3 v3, @Nullable final V4 v4) {
		checkNotNull(part1, "missing part1");
		return Tuples.of(part1.v1(), part1.v2(), v3, v4);
	}

	public static <V1, V2> Tuple2<V1, V2> of(@Nullable final V1 v1, @Nullable final V2 v2) {
		return new Tuple2<V1, V2>(v1, v2);
	}

	public static <V1, V2, V3> Tuple3<V1, V2, V3> of(@Nullable final V1 v1, @Nullable final V2 v2, @Nullable final V3 v3) {
		return new Tuple3<V1, V2, V3>(v1, v2, v3);
	}

	public static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> of(@Nullable final V1 v1, @Nullable final V2 v2, @Nullable final V3 v3, @Nullable final V4 v4) {
		return new Tuple4<V1, V2, V3, V4>(v1, v2, v3, v4);
	}

	public static <V1, V2, V3, V4, V5> Tuple5<V1, V2, V3, V4, V5> of(@Nullable final V1 v1, @Nullable final V2 v2, @Nullable final V3 v3, @Nullable final V4 v4,
			@Nullable final V5 v5) {
		return new Tuple5<V1, V2, V3, V4, V5>(v1, v2, v3, v4, v5);
	}

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
	public static boolean overlaps(final Tuple4<Integer, Integer, Integer, Integer> left, final Tuple4<Integer, Integer, Integer, Integer> right) {

		// If one rectangle is on left side of other
		if (left.v1() > right.v3() || right.v1() > left.v3()) {
			return false;
		}

		// If one rectangle is above other
		if (left.v2() > right.v4() || right.v2() > left.v4()) {
			return false;
		}

		return true;
	}

	// CONSTRUCTORS
	private Tuples() {
		throw new AssertionError("no instance");
	}
}