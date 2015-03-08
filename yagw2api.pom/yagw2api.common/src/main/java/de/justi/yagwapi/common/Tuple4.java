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

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;

public final class Tuple4<V1, V2, V3, V4> implements Tuple<V1, V4> {
	@Nullable
	private final V1 value1;
	@Nullable
	private final V2 value2;
	@Nullable
	private final V3 value3;
	@Nullable
	private final V4 value4;

	private transient final Supplier<List<Object>> valueListSupplier = Suppliers.memoize(new Supplier<List<Object>>() {
		@Override
		public List<Object> get() {
			return ImmutableList.copyOf(Tuple4.this.getValuesAsArray());
		}
	});

	Tuple4(@Nullable final V1 value1, @Nullable final V2 value2, @Nullable final V3 value3, @Nullable final V4 value4) {
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
		this.value4 = value4;
	}

	@Override
	public final List<Object> getValues() {
		return this.valueListSupplier.get();
	}

	@Override
	public final Object[] getValuesAsArray() {
		return new Object[] { this.value1, this.value2, this.value3, this.value4 };
	}

	@Override
	public final int getDimension() {
		return 4;
	}

	@Override
	public final Optional<V1> getFirstValue() {
		return Optional.fromNullable(this.value1);
	}

	@Override
	public final Optional<V4> getLastValue() {
		return Optional.fromNullable(this.value4);
	}

	public final Optional<V1> getValue1() {
		return Optional.fromNullable(this.value1);
	}

	public final Optional<V2> getValue2() {
		return Optional.fromNullable(this.value2);
	}

	public final Optional<V3> getValue3() {
		return Optional.fromNullable(this.value3);
	}

	public final Optional<V4> getValue4() {
		return Optional.fromNullable(this.value4);
	}

	public final Tuple4<V1, V2, V3, V4> setValue1(final V1 value) {
		return new Tuple4<V1, V2, V3, V4>(value, this.value2, this.value3, this.value4);
	}

	public final Tuple4<V1, V2, V3, V4> setValue2(final V2 value) {
		return new Tuple4<V1, V2, V3, V4>(this.value1, value, this.value3, this.value4);
	}

	public final Tuple4<V1, V2, V3, V4> setValue3(final V3 value) {
		return new Tuple4<V1, V2, V3, V4>(this.value1, this.value2, value, this.value4);
	}

	public final Tuple4<V1, V2, V3, V4> setValue4(final V4 value) {
		return new Tuple4<V1, V2, V3, V4>(this.value1, this.value2, this.value3, value);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("v1", this.value1).add("v2", this.value2).add("v3", this.value3).add("v4", this.value4).toString();
	}

	@Override
	public Iterator<Object> iterator() {
		return this.getValues().iterator();
	}
}
