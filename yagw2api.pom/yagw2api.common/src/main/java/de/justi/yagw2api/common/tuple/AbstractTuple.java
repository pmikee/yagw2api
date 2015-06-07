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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;

abstract class AbstractTuple implements Tuple {
	// FIELDS
	private final transient Supplier<List<Object>> valueListSupplier = Suppliers.memoize(new Supplier<List<Object>>() {
		@Override
		public List<Object> get() {
			return AbstractTuple.this.listBuilder().build();
		}
	});
	private final transient Supplier<Object[]> valueArraySupplier = Suppliers.memoize(new Supplier<Object[]>() {
		@Override
		public Object[] get() {
			return AbstractTuple.this.asList().toArray(new Object[AbstractTuple.this.dimension()]);
		}
	});
	private volatile transient Integer hashcode = null;

	// METHODS
	@Override
	public final int hashCode() {
		if (this.mayCacheHashCode()) {
			if (this.hashcode == null) {
				synchronized (this.hashcode) {
					if (this.hashcode == null) {
						this.hashcode = Objects.hash(this.asArray());
					}
				}
			}
			return this.hashcode;
		} else {
			return Objects.hash(this.asArray());
		}
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

	@Override
	public final Object[] asArray() {
		return this.valueArraySupplier.get();
	}

	@Override
	public final List<Object> asList() {
		return this.valueListSupplier.get();
	}

	@Override
	public final String toString() {
		return this.toStringHelper().toString();
	}

	protected ImmutableList.Builder<Object> listBuilder() {
		return ImmutableList.builder();
	}

	protected ToStringHelper toStringHelper() {
		return MoreObjects.toStringHelper("");
	}

}
