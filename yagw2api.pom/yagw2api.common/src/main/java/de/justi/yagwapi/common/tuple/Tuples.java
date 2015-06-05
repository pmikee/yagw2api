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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import de.justi.yagw2api.common.math.Math;

public final class Tuples {
	// STATIC METHODS

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

	// > NUMBER > UNIFORM TUPLES

	public static <V extends Number> UniformNumberTuple2<V> uniformOf(final V v1, final V v2) {
		return new DefaultUniformNumberTuple2<V>(checkNotNull(v1, "missing v1"), checkNotNull(v2, "missing v2"));
	}

	public static <V extends Number> UniformNumberTuple3<V> uniformOf(final V v1, final V v2, final V v3) {
		return new DefaultUniformNumberTuple3<V>(checkNotNull(v1, "missing v1"), checkNotNull(v2, "missing v2"), checkNotNull(v3, "missing v3"));
	}

	public static <V extends Number> UniformNumberTuple4<V> uniformOf(final V v1, final V v2, final V v3, final V v4) {
		return new DefaultUniformNumberTuple4<V>(checkNotNull(v1, "missing v1"), checkNotNull(v2, "missing v2"), checkNotNull(v3, "missing v3"), checkNotNull(v4, "missing v4"));
	}

	public static <V extends Number> UniformNumberTuple5<V> uniformOf(final V v1, final V v2, final V v3, final V v4, final V v5) {
		return new DefaultUniformNumberTuple5<V>(checkNotNull(v1, "missing v1"), checkNotNull(v2, "missing v2"), checkNotNull(v3, "missing v3"), checkNotNull(v4, "missing v4"),
				checkNotNull(v5, "missing v5"));
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

	// > NULLSUPPORTING > UNIFORM TUPLES

	public static <V> UniformTuple2<V> uniformOf(@Nullable final V v1, @Nullable final V v2) {
		return new DefaultUniformNullSupportingTuple2<V>(v1, v2);
	}

	public static <V> UniformTuple3<V> uniformOf(@Nullable final V v1, @Nullable final V v2, @Nullable final V v3) {
		return new DefaultUniformNullSupportingTuple3<V>(v1, v2, v3);
	}

	public static <V> UniformTuple4<V> uniformOf(@Nullable final V v1, @Nullable final V v2, @Nullable final V v3, @Nullable final V v4) {
		return new DefaultUniformNullSupportingTuple4<V>(v1, v2, v3, v4);
	}

	public static <V> UniformTuple5<V> uniformOf(@Nullable final V v1, @Nullable final V v2, @Nullable final V v3, @Nullable final V v4, @Nullable final V v5) {
		return new DefaultUniformNullSupportingTuple5<V>(v1, v2, v3, v4, v5);
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

	// > UNITILITIES > MATH > MULTIPLY
	public static UniformNumberTuple4<Integer> multiply(final NumberTuple4<?, ?, ?, ?> left, final int right) {
		return Tuples.uniformOf(left.v1().intValue() * right, left.v2().intValue() * right, left.v3().intValue() * right, left.v4().intValue() * right);
	}

	public static UniformNumberTuple4<Double> multiply(final NumberTuple4<?, ?, ?, ?> left, final double right) {
		return Tuples.uniformOf(left.v1().doubleValue() * right, left.v2().doubleValue() * right, left.v3().doubleValue() * right, left.v4().doubleValue() * right);
	}

	public static UniformNumberTuple4<Float> multiply(final NumberTuple4<?, ?, ?, ?> left, final float right) {
		return Tuples.uniformOf(left.v1().floatValue() * right, left.v2().floatValue() * right, left.v3().floatValue() * right, left.v4().floatValue() * right);
	}

	public static UniformNumberTuple4<Long> multiply(final NumberTuple4<?, ?, ?, ?> left, final long right) {
		return Tuples.uniformOf(left.v1().longValue() * right, left.v2().longValue() * right, left.v3().longValue() * right, left.v4().longValue() * right);
	}

	// > UTILITIES > GEOMETRY

	public static UniformNumberTuple4<Integer> rectangleFromTopLeftAndBottomRight(final UniformNumberTuple2<Integer> topLeft, final UniformNumberTuple2<Integer> bottomRight) {
		checkNotNull(topLeft, "missing topLeft");
		checkNotNull(bottomRight, "missing bottomRight");
		return rectangleFromTopLeftAndBottomRight(topLeft.v1(), topLeft.v2(), bottomRight.v1(), bottomRight.v2());
	}

	public static UniformNumberTuple4<Integer> rectangleFromTopLeftAndBottomRight(final int x1, final int y1, final int x2, final int y2) {
		checkArgument(x2 >= x1, "invalid x2: %s", x2);
		checkArgument(y2 >= y1, "invalid y2: %s", y2);
		checkArgument(x2 > x1 || y2 > y1, "invalid bottom right: %s/%s", x2, y2);
		return Tuples.uniformOf(x1, y1, x2, y2);
	}

	public static UniformNumberTuple4<Integer> rectangleFromTopLeftAndSize(final UniformNumberTuple2<Integer> topLeft, final UniformNumberTuple2<Integer> size) {
		checkNotNull(topLeft, "missing topLeft");
		checkNotNull(topLeft, "missing size");
		return rectangleFromTopLeftAndSize(topLeft.v1(), topLeft.v2(), size.v1(), size.v2());
	}

	public static UniformNumberTuple4<Integer> rectangleFromTopLeftAndSize(final UniformNumberTuple2<Integer> topLeft, final int width, final int height) {
		checkNotNull(topLeft, "missing topLeft");
		return rectangleFromTopLeftAndSize(topLeft.v1(), topLeft.v2(), width, height);
	}

	public static UniformNumberTuple4<Integer> rectangleFromTopLeftAndSize(final int x1, final int y1, final int width, final int height) {
		checkArgument(width >= 0, "invalid width: %s", width);
		checkArgument(width >= 0, "invalid height: %s", height);
		return Tuples.uniformOf(x1, y1, x1 + width, y1 + height);
	}

	// > UTILITIES > CLAMP

	public static <V extends Number> UniformNumberTuple2<V> clamp(final NumberTuple2<V, V> toClamp, final V min, final V max){
		return Tuples.uniformOf(Math.clamp(toClamp.v1(), min, max),Math.clamp(toClamp.v2(), min, max));
	}
	public static <V extends Number> UniformNumberTuple3<V> clamp(final NumberTuple3<V, V,V> toClamp, final V min, final V max){
		return Tuples.uniformOf(Math.clamp(toClamp.v1(), min, max),Math.clamp(toClamp.v2(), min, max),Math.clamp(toClamp.v3(), min, max));
	}
	public static <V extends Number> UniformNumberTuple4<V> clamp(final NumberTuple4<V, V,V,V> toClamp, final V min, final V max){
		return Tuples.uniformOf(Math.clamp(toClamp.v1(), min, max),Math.clamp(toClamp.v2(), min, max),Math.clamp(toClamp.v3(), min, max),Math.clamp(toClamp.v4(), min, max));
	}
	public static <V extends Number> UniformNumberTuple5<V> clamp(final NumberTuple5<V, V,V,V,V> toClamp, final V min, final V max){
		return Tuples.uniformOf(Math.clamp(toClamp.v1(), min, max),Math.clamp(toClamp.v2(), min, max),Math.clamp(toClamp.v3(), min, max),Math.clamp(toClamp.v4(), min, max),Math.clamp(toClamp.v5(), min, max));
	}

	// CONSTRUCTORS
	private Tuples() {
		throw new AssertionError("no instance");
	}
}