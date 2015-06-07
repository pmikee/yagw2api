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

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableList;

abstract class AbstractTuple2<V1, V2> extends AbstractTuple implements Tuple2<V1, V2> {
	// FIELDS
	@Nullable
	private final V1 v1;
	@Nullable
	private final V2 v2;

	// CONSTRUCTOR
	protected AbstractTuple2(@Nullable final V1 v1, @Nullable final V2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	// METHODS
	@Override
	@Nullable
	public final V1 v1() {
		return this.v1;
	}

	@Override
	@Nullable
	public final V2 v2() {
		return this.v2;
	}

	@Override
	protected ImmutableList.Builder<Object> listBuilder() {
		return super.listBuilder().add(this.v1).add(this.v2);
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().addValue(this.v1).addValue(this.v2);
	}
}