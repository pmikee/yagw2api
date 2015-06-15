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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.annotation.Nullable;

public final class Tuples {
	// STATIC METHODS

	public static <V> Tuple from(final List<V> parts) {
		checkNotNull(parts, "missing parts");
		switch (parts.size()) {
			case 2:
				return uniformOf(parts.get(0), parts.get(1));
			case 3:
				return uniformOf(parts.get(0), parts.get(1), parts.get(2));
			case 4:
				return uniformOf(parts.get(0), parts.get(1), parts.get(2), parts.get(3));
			case 5:
				return uniformOf(parts.get(0), parts.get(1), parts.get(2), parts.get(3), parts.get(4));
			default:
				throw new IllegalArgumentException("Unsupported part count: " + parts.size());
		}
	}

	// > PRIMITIVES
	public static LongTuple2 of(final long v1, final long v2) {
		return new LongArrayTuple(v1, v2);
	}

	public static IntTuple2 of(final int v1, final int v2) {
		return new IntArrayTuple(v1, v2);
	}

	public static DoubleTuple2 of(final double v1, final double v2) {
		return new DoubleArrayTuple(v1, v2);
	}

	public static FloatTuple2 of(final float v1, final float v2) {
		return new FloatArrayTuple(v1, v2);
	}

	public static LongTuple3 of(final long v1, final long v2, final long v3) {
		return new LongArrayTuple(v1, v2, v3);
	}

	public static IntTuple3 of(final int v1, final int v2, final int v3) {
		return new IntArrayTuple(v1, v2, v3);
	}

	public static DoubleTuple3 of(final double v1, final double v2, final double v3) {
		return new DoubleArrayTuple(v1, v2, v3);
	}

	public static FloatTuple3 of(final float v1, final float v2, final float v3) {
		return new FloatArrayTuple(v1, v2, v3);
	}

	public static LongTuple4 of(final long v1, final long v2, final long v3, final long v4) {
		return new LongArrayTuple(v1, v2, v3, v4);
	}

	public static IntTuple4 of(final int v1, final int v2, final int v3, final int v4) {
		return new IntArrayTuple(v1, v2, v3, v4);
	}

	public static DoubleTuple4 of(final double v1, final double v2, final double v3, final double v4) {
		return new DoubleArrayTuple(v1, v2, v3, v4);
	}

	public static FloatTuple4 of(final float v1, final float v2, final float v3, final float v4) {
		return new FloatArrayTuple(v1, v2, v3, v4);
	}

	public static LongTuple5 of(final long v1, final long v2, final long v3, final long v4, final long v5) {
		return new LongArrayTuple(v1, v2, v3, v4, v5);
	}

	public static IntTuple5 of(final int v1, final int v2, final int v3, final int v4, final int v5) {
		return new IntArrayTuple(v1, v2, v3, v4, v5);
	}

	public static DoubleTuple5 of(final double v1, final double v2, final double v3, final double v4, final double v5) {
		return new DoubleArrayTuple(v1, v2, v3, v4, v5);
	}

	public static FloatTuple5 of(final float v1, final float v2, final float v3, final float v4, final float v5) {
		return new FloatArrayTuple(v1, v2, v3, v4, v5);
	}

	// > NUMBER TUPLES
	@SuppressWarnings("unchecked")
	public static <V1 extends Number, V2 extends Number> NumberTuple2<V1, V2> of(final V1 v1, final V2 v2) {
		checkNotNull(v1, "missing v1");
		checkNotNull(v2, "missing v2");

		if (v1.getClass().isInstance(v2)) {
			return (NumberTuple2<V1, V2>) uniformOf(v1, (V1) v2);
		} else if (v2.getClass().isInstance(v1)) {
			return (NumberTuple2<V1, V2>) uniformOf((V2) v1, v2);
		} else {
			return new DefaultNumberTuple2<V1, V2>(v1, v2);
		}
	}

	@SuppressWarnings("unchecked")
	public static <V1 extends Number, V2 extends Number, V3 extends Number> NumberTuple3<V1, V2, V3> of(final V1 v1, final V2 v2, final V3 v3) {
		checkNotNull(v1, "missing v1");
		checkNotNull(v2, "missing v2");
		checkNotNull(v3, "missing v3");

		if (v1.getClass().isInstance(v2) && v1.getClass().isInstance(v3)) {
			return (NumberTuple3<V1, V2, V3>) uniformOf(v1, (V1) v2, (V1) v3);
		} else if (v2.getClass().isInstance(v1) && v2.getClass().isInstance(v3)) {
			return (NumberTuple3<V1, V2, V3>) uniformOf((V2) v1, v2, (V2) v3);
		} else if (v3.getClass().isInstance(v1) && v3.getClass().isInstance(v2)) {
			return (NumberTuple3<V1, V2, V3>) uniformOf((V3) v1, (V3) v2, v3);
		} else {
			return new DefaultNumberTuple3<V1, V2, V3>(v1, v2, v3);
		}
	}

	@SuppressWarnings("unchecked")
	public static <V1 extends Number, V2 extends Number, V3 extends Number, V4 extends Number> NumberTuple4<V1, V2, V3, V4> of(final V1 v1, final V2 v2, final V3 v3, final V4 v4) {
		checkNotNull(v1, "missing v1");
		checkNotNull(v2, "missing v2");
		checkNotNull(v3, "missing v3");
		checkNotNull(v4, "missing v4");

		if (v1.getClass().isInstance(v2) && v1.getClass().isInstance(v3) && v1.getClass().isInstance(v4)) {
			return (NumberTuple4<V1, V2, V3, V4>) uniformOf(v1, (V1) v2, (V1) v3, (V1) v4);
		} else if (v2.getClass().isInstance(v1) && v2.getClass().isInstance(v3) && v2.getClass().isInstance(v4)) {
			return (NumberTuple4<V1, V2, V3, V4>) uniformOf((V2) v1, v2, (V2) v3, (V2) v4);
		} else if (v3.getClass().isInstance(v1) && v3.getClass().isInstance(v2) && v3.getClass().isInstance(v4)) {
			return (NumberTuple4<V1, V2, V3, V4>) uniformOf((V3) v1, (V3) v2, v3, (V3) v4);
		} else if (v4.getClass().isInstance(v1) && v4.getClass().isInstance(v2) && v4.getClass().isInstance(v3)) {
			return (NumberTuple4<V1, V2, V3, V4>) uniformOf((V4) v1, (V4) v2, (V4) v3, v4);
		} else {
			return new DefaultNumberTuple4<V1, V2, V3, V4>(v1, v2, v3, v4);
		}
	}

	@SuppressWarnings("unchecked")
	public static <V1 extends Number, V2 extends Number, V3 extends Number, V4 extends Number, V5 extends Number> NumberTuple5<V1, V2, V3, V4, V5> of(final V1 v1, final V2 v2,
			final V3 v3, final V4 v4, final V5 v5) {
		checkNotNull(v1, "missing v1");
		checkNotNull(v2, "missing v2");
		checkNotNull(v3, "missing v3");
		checkNotNull(v4, "missing v4");
		checkNotNull(v5, "missing v5");
		if (v1.getClass().isInstance(v2) && v1.getClass().isInstance(v3) && v1.getClass().isInstance(v4) && v1.getClass().isInstance(v5)) {
			return (NumberTuple5<V1, V2, V3, V4, V5>) uniformOf(v1, (V1) v2, (V1) v3, (V1) v4, (V1) v5);
		} else if (v2.getClass().isInstance(v1) && v2.getClass().isInstance(v3) && v2.getClass().isInstance(v4) && v2.getClass().isInstance(v5)) {
			return (NumberTuple5<V1, V2, V3, V4, V5>) uniformOf((V2) v1, v2, (V2) v3, (V2) v4, (V2) v5);
		} else if (v3.getClass().isInstance(v1) && v3.getClass().isInstance(v2) && v3.getClass().isInstance(v4) && v3.getClass().isInstance(v5)) {
			return (NumberTuple5<V1, V2, V3, V4, V5>) uniformOf((V3) v1, (V3) v2, v3, (V3) v4, (V3) v5);
		} else if (v4.getClass().isInstance(v1) && v4.getClass().isInstance(v2) && v4.getClass().isInstance(v3) && v4.getClass().isInstance(v5)) {
			return (NumberTuple5<V1, V2, V3, V4, V5>) uniformOf((V4) v1, (V4) v2, (V4) v3, v4, (V4) v5);
		} else if (v5.getClass().isInstance(v1) && v5.getClass().isInstance(v2) && v5.getClass().isInstance(v3) && v5.getClass().isInstance(v4)) {
			return (NumberTuple5<V1, V2, V3, V4, V5>) uniformOf((V5) v1, (V5) v2, (V5) v3, (V5) v4, v5);
		} else {
			return new DefaultNumberTuple5<V1, V2, V3, V4, V5>(v1, v2, v3, v4, v5);
		}
	}

	// > NUMBER > UNIFORM TUPLES

	@SuppressWarnings("unchecked")
	public static <V extends Number> UniformNumberTuple2<V> uniformOf(final V v1, final V v2) {
		checkNotNull(v1, "missing v1");
		checkNotNull(v2, "missing v2");
		if (v1 instanceof Integer) {
			return (UniformNumberTuple2<V>) of(v1.intValue(), v2.intValue());
		} else if (v1 instanceof Double) {
			return (UniformNumberTuple2<V>) of(v1.doubleValue(), v2.doubleValue());
		} else if (v1 instanceof Float) {
			return (UniformNumberTuple2<V>) of(v1.floatValue(), v2.floatValue());
		} else if (v1 instanceof Long) {
			return (UniformNumberTuple2<V>) of(v1.longValue(), v2.longValue());
		} else {
			return new DefaultUniformNumberTuple2<V>(v1, v2);
		}
	}

	@SuppressWarnings("unchecked")
	public static <V extends Number> UniformNumberTuple3<V> uniformOf(final V v1, final V v2, final V v3) {
		checkNotNull(v1, "missing v1");
		checkNotNull(v2, "missing v2");
		checkNotNull(v3, "missing v3");
		if (v1 instanceof Integer) {
			return (UniformNumberTuple3<V>) of(v1.intValue(), v2.intValue(), v3.intValue());
		} else if (v1 instanceof Double) {
			return (UniformNumberTuple3<V>) of(v1.doubleValue(), v2.doubleValue(), v3.doubleValue());
		} else if (v1 instanceof Float) {
			return (UniformNumberTuple3<V>) of(v1.floatValue(), v2.floatValue(), v3.floatValue());
		} else if (v1 instanceof Long) {
			return (UniformNumberTuple3<V>) of(v1.longValue(), v2.longValue(), v3.longValue());
		} else {
			return new DefaultUniformNumberTuple3<V>(v1, v2, v3);
		}
	}

	@SuppressWarnings("unchecked")
	public static <V extends Number> UniformNumberTuple4<V> uniformOf(final V v1, final V v2, final V v3, final V v4) {
		checkNotNull(v1, "missing v1");
		checkNotNull(v2, "missing v2");
		checkNotNull(v3, "missing v3");
		checkNotNull(v4, "missing v4");
		if (v1 instanceof Integer) {
			return (UniformNumberTuple4<V>) of(v1.intValue(), v2.intValue(), v3.intValue(), v4.intValue());
		} else if (v1 instanceof Double) {
			return (UniformNumberTuple4<V>) of(v1.doubleValue(), v2.doubleValue(), v3.doubleValue(), v4.doubleValue());
		} else if (v1 instanceof Float) {
			return (UniformNumberTuple4<V>) of(v1.floatValue(), v2.floatValue(), v3.floatValue(), v4.floatValue());
		} else if (v1 instanceof Long) {
			return (UniformNumberTuple4<V>) of(v1.longValue(), v2.longValue(), v3.longValue(), v4.longValue());
		} else {
			return new DefaultUniformNumberTuple4<V>(v1, v2, v3, v4);
		}
	}

	@SuppressWarnings("unchecked")
	public static <V extends Number> UniformNumberTuple5<V> uniformOf(final V v1, final V v2, final V v3, final V v4, final V v5) {
		checkNotNull(v1, "missing v1");
		checkNotNull(v2, "missing v2");
		checkNotNull(v3, "missing v3");
		checkNotNull(v4, "missing v4");
		checkNotNull(v5, "missing v5");
		if (v1 instanceof Integer) {
			return (UniformNumberTuple5<V>) of(v1.intValue(), v2.intValue(), v3.intValue(), v4.intValue(), v5.intValue());
		} else if (v1 instanceof Double) {
			return (UniformNumberTuple5<V>) of(v1.doubleValue(), v2.doubleValue(), v3.doubleValue(), v4.doubleValue(), v4.doubleValue());
		} else if (v1 instanceof Float) {
			return (UniformNumberTuple5<V>) of(v1.floatValue(), v2.floatValue(), v3.floatValue(), v4.floatValue(), v4.floatValue());
		} else if (v1 instanceof Long) {
			return (UniformNumberTuple5<V>) of(v1.longValue(), v2.longValue(), v3.longValue(), v4.longValue(), v4.longValue());
		} else {
			return new DefaultUniformNumberTuple5<V>(v1, v2, v3, v4, v5);
		}
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

	// > UTILITIES > GEOMETRY

	public static IntTuple4 rectangleFromTopLeftAndBottomRight(final UniformNumberTuple2<Integer> topLeft, final UniformNumberTuple2<Integer> bottomRight) {
		checkNotNull(topLeft, "missing topLeft");
		checkNotNull(bottomRight, "missing bottomRight");
		return rectangleFromTopLeftAndBottomRight(topLeft.v1(), topLeft.v2(), bottomRight.v1(), bottomRight.v2());
	}

	public static IntTuple4 rectangleFromTopLeftAndBottomRight(final int x1, final int y1, final int x2, final int y2) {
		checkArgument(x2 >= x1, "invalid x2: %s", x2);
		checkArgument(y2 >= y1, "invalid y2: %s", y2);
		checkArgument(x2 > x1 || y2 > y1, "invalid bottom right: %s/%s", x2, y2);
		return Tuples.of(x1, y1, x2, y2);
	}

	public static IntTuple4 rectangleFromTopLeftAndSize(final UniformNumberTuple2<Integer> topLeft, final UniformNumberTuple2<Integer> size) {
		checkNotNull(topLeft, "missing topLeft");
		checkNotNull(topLeft, "missing size");
		return rectangleFromTopLeftAndSize(topLeft.v1(), topLeft.v2(), size.v1(), size.v2());
	}

	public static IntTuple4 rectangleFromTopLeftAndSize(final UniformNumberTuple2<Integer> topLeft, final int width, final int height) {
		checkNotNull(topLeft, "missing topLeft");
		return rectangleFromTopLeftAndSize(topLeft.v1(), topLeft.v2(), width, height);
	}

	public static IntTuple4 rectangleFromTopLeftAndSize(final int x1, final int y1, final int width, final int height) {
		checkArgument(width >= 0, "invalid width: %s", width);
		checkArgument(width >= 0, "invalid height: %s", height);
		return Tuples.of(x1, y1, x1 + width, y1 + height);
	}

	// CONSTRUCTORS
	private Tuples() {
		throw new AssertionError("no instance");
	}
}