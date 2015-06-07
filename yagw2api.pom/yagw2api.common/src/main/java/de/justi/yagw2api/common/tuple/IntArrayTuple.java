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

import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableList;

final class IntArrayTuple extends AbstractTuple implements IntTuple2, IntTuple3, IntTuple4, IntTuple5 {
	// FIELDS
	private final int[] values;

	// CONSTRUCTOR

	public IntArrayTuple(final int... values) {
		this.values = values;
	}

	// METHODS
	@Override
	public boolean mayCacheHashCode() {
		return true;
	}

	@Override
	protected final ImmutableList.Builder<Object> listBuilder() {
		final ImmutableList.Builder<Object> builder = super.listBuilder();
		for (int value : this.values) {
			builder.add(value);
		}
		return builder;
	}

	@Override
	protected final ToStringHelper toStringHelper() {
		final ToStringHelper helper = super.toStringHelper();
		for (int value : this.values) {
			helper.addValue(value);
		}
		return helper;
	}

	@Override
	public final int dimension() {
		return this.values.length;
	}

	@Override
	public final int[] asIntArray() {
		return this.values;
	}

	@Override
	public Integer v1() {
		checkState(this.dimension() >= 1, "invalid dimension: %s", this.dimension());
		return this.values[0];
	}

	@Override
	public Integer v2() {
		checkState(this.dimension() >= 2, "invalid dimension: %s", this.dimension());
		return this.values[1];
	}

	@Override
	public Integer v3() {
		checkState(this.dimension() >= 3, "invalid dimension: %s", this.dimension());
		return this.values[2];
	}

	@Override
	public Integer v4() {
		checkState(this.dimension() >= 4, "invalid dimension: %s", this.dimension());
		return this.values[3];
	}

	@Override
	public Integer v5() {
		checkState(this.dimension() >= 5, "invalid dimension: %s", this.dimension());
		return this.values[4];
	}

	@Override
	public final int v1Int() {
		checkState(this.dimension() >= 1, "invalid dimension: %s", this.dimension());
		return this.values[0];
	}

	@Override
	public final int v2Int() {
		checkState(this.dimension() >= 2, "invalid dimension: %s", this.dimension());
		return this.values[1];
	}

	@Override
	public int v3Int() {
		checkState(this.dimension() >= 3, "invalid dimension: %s", this.dimension());
		return this.values[2];
	}

	@Override
	public int v4Int() {
		checkState(this.dimension() >= 4, "invalid dimension: %s", this.dimension());
		return this.values[3];
	}

	@Override
	public int v5Int() {
		checkState(this.dimension() >= 5, "invalid dimension: %s", this.dimension());
		return this.values[4];
	}
}
