package yagw2api.common.tuple;

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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.justi.yagw2api.common.tuple.DoubleTuple2;
import de.justi.yagw2api.common.tuple.FloatTuple2;
import de.justi.yagw2api.common.tuple.IntTuple2;
import de.justi.yagw2api.common.tuple.LongTuple2;
import de.justi.yagw2api.common.tuple.Tuples;

@RunWith(Parameterized.class)
public class TestIntTuple2 {

	@Parameters(name = "v1={0}, v2={1}")
	public static final Object[][] paramters() {
		return new Object[][] {
				new Integer[] { 0, 0 },
				new Integer[] { -1, 0 },
				new Integer[] { 0, -1 },
				new Integer[] { -1, -1 },
				new Integer[] { 1, 0 },
				new Integer[] { 0, 1 },
				new Integer[] { 1, 1 },
				new Integer[] { -1, 1 },
				new Integer[] { 1, -1 },
				new Integer[] { 0, Integer.MIN_VALUE },
				new Integer[] { Integer.MIN_VALUE, 0 },
				new Integer[] { Integer.MIN_VALUE, Integer.MIN_VALUE },
				new Integer[] { 0, Integer.MAX_VALUE },
				new Integer[] { Integer.MAX_VALUE, 0 },
				new Integer[] { Integer.MAX_VALUE, Integer.MAX_VALUE },
				new Integer[] { Integer.MIN_VALUE, Integer.MAX_VALUE } };
	}

	private final int v1;
	private final int v2;

	public TestIntTuple2(final int v1, final int v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	@Test
	public void testClampToZero() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 clamped = tuple2.clampTuple2(0, 0);

		assertThat(clamped.v1(), is(0));
		assertThat(clamped.v2(), is(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testClampToInvalid() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 clamped = tuple2.clampTuple2(1, -1);

		assertThat(clamped.v1(), is(0));
		assertThat(clamped.v2(), is(0));
	}

	@Test
	public void testClampToMaxInt() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 clamped = tuple2.clampTuple2(Integer.MAX_VALUE, Integer.MAX_VALUE);

		assertThat(clamped.v1(), is(Integer.MAX_VALUE));
		assertThat(clamped.v2(), is(Integer.MAX_VALUE));
	}

	@Test
	public void testClampToMinInt() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 clamped = tuple2.clampTuple2(Integer.MIN_VALUE, Integer.MIN_VALUE);

		assertThat(clamped.v1(), is(Integer.MIN_VALUE));
		assertThat(clamped.v2(), is(Integer.MIN_VALUE));
	}

	@Test
	public void testClampToIntRange() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 clamped = tuple2.clampTuple2(Integer.MIN_VALUE, Integer.MAX_VALUE);

		assertThat(clamped.v1(), is(this.v1));
		assertThat(clamped.v2(), is(this.v2));
	}

	@Test
	public void testClampToV1() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 clamped = tuple2.clampTuple2(this.v1, this.v1);

		assertThat(clamped.v1(), is(this.v1));
		assertThat(clamped.v2(), is(this.v1));
	}

	@Test
	public void testClampToV2() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 clamped = tuple2.clampTuple2(this.v2, this.v2);

		assertThat(clamped.v1(), is(this.v2));
		assertThat(clamped.v2(), is(this.v2));
	}

	@Test
	public void testClampToOriginalRange() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final int lower;
		final int upper;
		if (this.v1 <= this.v2) {
			lower = this.v1;
			upper = this.v2;
		} else {
			lower = this.v2;
			upper = this.v1;
		}
		final IntTuple2 clamped = tuple2.clampTuple2(lower, upper);

		assertThat(clamped.v1(), is(this.v1));
		assertThat(clamped.v2(), is(this.v2));
	}

	@Test
	public void testMultiplyByMinusOne() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 multiplied = tuple2.multiplyTuple2(-1);

		assertThat(multiplied.v1(), is(-tuple2.v1()));
		assertThat(multiplied.v2(), is(-tuple2.v2()));
	}

	@Test
	public void testMultiplyByZero() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 multiplied = tuple2.multiplyTuple2(0);

		assertThat(multiplied.v1(), is(0));
		assertThat(multiplied.v2(), is(0));
	}

	@Test
	public void testMultiplyByOne() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 multiplied = tuple2.multiplyTuple2(1);

		assertThat(multiplied.v1(), is(tuple2.v1()));
		assertThat(multiplied.v2(), is(tuple2.v2()));
	}

	@Test
	public void testMultiplyByTwo() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 multiplied = tuple2.multiplyTuple2(2);

		assertThat(multiplied.v1(), is(2 * tuple2.v1()));
		assertThat(multiplied.v2(), is(2 * tuple2.v2()));
	}

	@Test
	public void testMultiplyByIntMin() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 multiplied = tuple2.multiplyTuple2(Integer.MIN_VALUE);

		assertThat(multiplied.v1(), is(Integer.MIN_VALUE * tuple2.v1()));
		assertThat(multiplied.v2(), is(Integer.MIN_VALUE * tuple2.v2()));
	}

	@Test
	public void testMultiplyByIntMax() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 multiplied = tuple2.multiplyTuple2(Integer.MAX_VALUE);

		assertThat(multiplied.v1(), is(Integer.MAX_VALUE * tuple2.v1()));
		assertThat(multiplied.v2(), is(Integer.MAX_VALUE * tuple2.v2()));
	}

	@Test
	public void testMultiplyByIntMinMax() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 multiplied = tuple2.multiplyTuple2(Integer.MIN_VALUE, Integer.MAX_VALUE);

		assertThat(multiplied.v1(), is(Integer.MIN_VALUE * tuple2.v1()));
		assertThat(multiplied.v2(), is(Integer.MAX_VALUE * tuple2.v2()));
	}

	@Test
	public void testMultiplyByLongMin() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final LongTuple2 multiplied = tuple2.multiplyTuple2(Long.MIN_VALUE);

		assertThat(multiplied.v1(), is(Long.MIN_VALUE * tuple2.v1()));
		assertThat(multiplied.v2(), is(Long.MIN_VALUE * tuple2.v2()));
	}

	@Test
	public void testMultiplyByLongMax() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final LongTuple2 multiplied = tuple2.multiplyTuple2(Long.MAX_VALUE);

		assertThat(multiplied.v1(), is(Long.MAX_VALUE * tuple2.v1()));
		assertThat(multiplied.v2(), is(Long.MAX_VALUE * tuple2.v2()));
	}

	@Test
	public void testMultiplyByLongMinMax() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final LongTuple2 multiplied = tuple2.multiplyTuple2(Long.MIN_VALUE, Long.MAX_VALUE);

		assertThat(multiplied.v1(), is(Long.MIN_VALUE * tuple2.v1()));
		assertThat(multiplied.v2(), is(Long.MAX_VALUE * tuple2.v2()));
	}

	@Test
	public void testMultiplyByDoubleMin() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final DoubleTuple2 multiplied = tuple2.multiplyTuple2(Double.MIN_VALUE);

		assertThat(multiplied.v1(), is(Double.MIN_VALUE * tuple2.v1()));
		assertThat(multiplied.v2(), is(Double.MIN_VALUE * tuple2.v2()));
	}

	@Test
	public void testMultiplyByDoubleMax() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final DoubleTuple2 multiplied = tuple2.multiplyTuple2(Double.MAX_VALUE);

		assertThat(multiplied.v1(), is(Double.MAX_VALUE * tuple2.v1()));
		assertThat(multiplied.v2(), is(Double.MAX_VALUE * tuple2.v2()));
	}

	@Test
	public void testMultiplyByDoubleMinMax() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final DoubleTuple2 multiplied = tuple2.multiplyTuple2(Double.MIN_VALUE, Double.MAX_VALUE);

		assertThat(multiplied.v1(), is(Double.MIN_VALUE * tuple2.v1()));
		assertThat(multiplied.v2(), is(Double.MAX_VALUE * tuple2.v2()));
	}

	@Test
	public void testMultiplyByFloatMin() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final FloatTuple2 multiplied = tuple2.multiplyTuple2(Float.MIN_VALUE);

		assertThat(multiplied.v1(), is(Float.MIN_VALUE * tuple2.v1()));
		assertThat(multiplied.v2(), is(Float.MIN_VALUE * tuple2.v2()));
	}

	@Test
	public void testMultiplyByFloatMax() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final FloatTuple2 multiplied = tuple2.multiplyTuple2(Float.MAX_VALUE);

		assertThat(multiplied.v1(), is(Float.MAX_VALUE * tuple2.v1()));
		assertThat(multiplied.v2(), is(Float.MAX_VALUE * tuple2.v2()));
	}

	@Test
	public void testMultiplyByFloatMinMax() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final FloatTuple2 multiplied = tuple2.multiplyTuple2(Float.MIN_VALUE, Float.MAX_VALUE);

		assertThat(multiplied.v1(), is(Float.MIN_VALUE * tuple2.v1()));
		assertThat(multiplied.v2(), is(Float.MAX_VALUE * tuple2.v2()));
	}

	@Test
	public void testConvertionToIntTuple2() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final IntTuple2 converted = tuple2.asIntTuple2();

		assertThat(tuple2.v1(), is(converted.v1()));
		assertThat(tuple2.v2(), is(converted.v2()));
		assertThat(converted.v1(), is(this.v1));
		assertThat(converted.v2(), is(this.v2));
	}

	@Test
	public void testConvertionToDoubleTuple2() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final DoubleTuple2 converted = tuple2.asDoubleTuple2();

		assertThat(tuple2.v1().doubleValue(), is(converted.v1()));
		assertThat(tuple2.v2().doubleValue(), is(converted.v2()));
		assertThat(converted.v1(), is((double) this.v1));
		assertThat(converted.v2(), is((double) this.v2));
	}

	@Test
	public void testConvertionToLongTuple2() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final LongTuple2 converted = tuple2.asLongTuple2();

		assertThat(tuple2.v1().longValue(), is(converted.v1()));
		assertThat(tuple2.v2().longValue(), is(converted.v2()));
		assertThat(converted.v1(), is((long) this.v1));
		assertThat(converted.v2(), is((long) this.v2));
	}

	@Test
	public void testConvertionToFloatTuple2() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final FloatTuple2 converted = tuple2.asFloatTuple2();

		assertThat(tuple2.v1().floatValue(), is(converted.v1()));
		assertThat(tuple2.v2().floatValue(), is(converted.v2()));
		assertThat(converted.v1(), is((float) this.v1));
		assertThat(converted.v2(), is((float) this.v2));
	}

	@Test
	public void testLongValues() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);

		assertThat(tuple2.v1Long(), is((long) this.v1));
		assertThat(tuple2.v2Long(), is((long) this.v2));
	}

	@Test
	public void testIntValues() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);

		assertThat(tuple2.v1Int(), is(this.v1));
		assertThat(tuple2.v2Int(), is(this.v2));
	}

	@Test
	public void testValues() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);

		assertThat(tuple2.v1(), is(this.v1));
		assertThat(tuple2.v2(), is(this.v2));
	}

	@Test
	public void testDoubleValues() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);

		assertThat(tuple2.v1Double(), is((double) this.v1));
		assertThat(tuple2.v2Double(), is((double) this.v2));
	}

	@Test
	public void testFloatValues() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);

		assertThat(tuple2.v1Float(), is((float) this.v1));
		assertThat(tuple2.v2Float(), is((float) this.v2));
	}

	@Test
	public void testAsIntArray() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final int[] intArray = tuple2.asIntArray();

		assertThat(intArray.length, is(2));
		assertThat(intArray[0], is(this.v1));
		assertThat(intArray[1], is(this.v2));
	}

	@Test
	public void testAsArray() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final Object[] array = tuple2.asArray();

		assertThat(array.length, is(2));
		assertThat(array[0], is(this.v1));
		assertThat(array[1], is(this.v2));
	}

	@Test
	public void testAsList() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		final List<Object> array = tuple2.asList();

		assertThat(array.size(), is(2));
		assertThat(array.get(0), is(this.v1));
		assertThat(array.get(1), is(this.v2));
	}

	@Test
	public void testDimension() {
		final IntTuple2 tuple2 = Tuples.of(this.v1, this.v2);
		assertThat(tuple2.dimension(), is(2));
	}
}
