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

final class LongArrayTuple extends AbstractTuple implements LongTuple2, LongTuple3, LongTuple4, LongTuple5 {
	// FIELDS
	private final long[] values;

	// CONSTRUCTOR

	public LongArrayTuple(final long... values) {
		this.values = values;
	}

	// METHODS

	@Override
	protected final ImmutableList.Builder<Object> listBuilder() {
		final ImmutableList.Builder<Object> builder = super.listBuilder();
		for (long value : this.values) {
			builder.add(value);
		}
		return builder;
	}

	@Override
	protected final ToStringHelper toStringHelper() {
		final ToStringHelper helper = super.toStringHelper();
		for (long value : this.values) {
			helper.addValue(value);
		}
		return helper;
	}

	@Override
	public final int dimension() {
		return this.values.length;
	}

	@Override
	public final long[] asLongArray() {
		return this.values;
	}

	@Override
	public Long v1() {
		checkState(this.dimension() >= 1, "invalid dimension: %s", this.dimension());
		return this.values[0];
	}

	@Override
	public Long v2() {
		checkState(this.dimension() >= 2, "invalid dimension: %s", this.dimension());
		return this.values[1];
	}

	@Override
	public Long v3() {
		checkState(this.dimension() >= 3, "invalid dimension: %s", this.dimension());
		return this.values[2];
	}

	@Override
	public Long v4() {
		checkState(this.dimension() >= 4, "invalid dimension: %s", this.dimension());
		return this.values[3];
	}

	@Override
	public Long v5() {
		checkState(this.dimension() >= 5, "invalid dimension: %s", this.dimension());
		return this.values[4];
	}

	@Override
	public final long v1Long() {
		checkState(this.dimension() >= 1, "invalid dimension: %s", this.dimension());
		return this.values[0];
	}

	@Override
	public final long v2Long() {
		checkState(this.dimension() >= 2, "invalid dimension: %s", this.dimension());
		return this.values[1];
	}

	@Override
	public long v3Long() {
		checkState(this.dimension() >= 3, "invalid dimension: %s", this.dimension());
		return this.values[2];
	}

	@Override
	public long v4Long() {
		checkState(this.dimension() >= 4, "invalid dimension: %s", this.dimension());
		return this.values[3];
	}

	@Override
	public long v5Long() {
		checkState(this.dimension() >= 5, "invalid dimension: %s", this.dimension());
		return this.values[4];
	}
}
