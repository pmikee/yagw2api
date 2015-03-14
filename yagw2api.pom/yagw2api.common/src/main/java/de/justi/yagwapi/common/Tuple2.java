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

public final class Tuple2<V1, V2> implements Tuple<V1, V2> {
	@Nullable
	private final V1 value1;
	@Nullable
	private final V2 value2;

	private transient final Supplier<List<Object>> valueListSupplier = Suppliers.memoize(new Supplier<List<Object>>() {
		@Override
		public List<Object> get() {
			return ImmutableList.copyOf(Tuple2.this.asArray());
		}
	});

	Tuple2(@Nullable final V1 value1, @Nullable final V2 value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

	@Override
	public final List<Object> asList() {
		return this.valueListSupplier.get();
	}

	@Override
	public final Object[] asArray() {
		return new Object[] { this.value1, this.value2 };
	}

	@Override
	public final int dimension() {
		return 2;
	}

	@Override
	public final Optional<V1> first() {
		return Optional.fromNullable(this.value1);
	}

	@Override
	public final Optional<V2> last() {
		return Optional.fromNullable(this.value2);
	}

	public final Optional<V1> getValue1() {
		return Optional.fromNullable(this.value1);
	}

	public final Tuple2<V1, V2> setValue1(final V1 value) {
		return new Tuple2<V1, V2>(value, this.value2);
	}

	public final Optional<V2> getValue2() {
		return Optional.fromNullable(this.value2);
	}

	public final Tuple2<V1, V2> setValue2(final V2 value) {
		return new Tuple2<V1, V2>(this.value1, value);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("v1", this.value1).add("v2", this.value2).toString();
	}

	@Override
	public Iterator<Object> iterator() {
		return this.asList().iterator();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.value1 == null) ? 0 : this.value1.hashCode());
		result = prime * result + ((this.value2 == null) ? 0 : this.value2.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Tuple2))
			return false;
		Tuple2 other = (Tuple2) obj;
		if (this.value1 == null) {
			if (other.value1 != null)
				return false;
		} else if (!this.value1.equals(other.value1))
			return false;
		if (this.value2 == null) {
			if (other.value2 != null)
				return false;
		} else if (!this.value2.equals(other.value2))
			return false;
		return true;
	}
}
