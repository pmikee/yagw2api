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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;

abstract class AbstractTuple2<V1, V2> implements Tuple2<V1, V2> {
	// FIELDS
	@Nullable
	private final V1 value1;
	@Nullable
	private final V2 value2;

	private final transient Supplier<List<Object>> valueListSupplier = Suppliers.memoize(new Supplier<List<Object>>() {
		@Override
		public List<Object> get() {
			return AbstractTuple2.this.listBuilder().build();
		}
	});
	private final transient Supplier<Object[]> valueArraySupplier = Suppliers.memoize(new Supplier<Object[]>() {
		@Override
		public Object[] get() {
			return AbstractTuple2.this.asList().toArray(new Object[AbstractTuple2.this.dimension()]);
		}
	});

	// CONSTRUCTOR
	protected AbstractTuple2(@Nullable final V1 value1, @Nullable final V2 value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

	// METHODS
	@Override
	public final List<Object> asList() {
		return this.valueListSupplier.get();
	}

	@Override
	public final Object[] asArray() {
		return this.valueArraySupplier.get();
	}

	@Override
	public final int dimension() {
		return this.asList().size();
	}

	@Override
	@Nullable
	public final V1 v1() {
		return this.value1;
	}

	@Override
	@Nullable
	public final V2 v2() {
		return this.value2;
	}

	@Override
	public final String toString() {
		return this.toStringHelper().toString();
	}

	@Override
	public final int hashCode() {
		return Objects.hash(this.asArray());
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof Tuple) {
			final Tuple other = (Tuple) obj;
			return Arrays.equals(this.asArray(), other.asArray());
		} else {
			return false;
		}
	}

	protected ImmutableList.Builder<Object> listBuilder() {
		final ImmutableList.Builder<Object> builder = ImmutableList.builder();
		builder.add(this.value1);
		builder.add(this.value2);
		return builder;
	}

	protected ToStringHelper toStringHelper() {
		return MoreObjects.toStringHelper("").addValue(this.value1).addValue(this.value2);
	}
}