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

public class AbstractTuple3<V1, V2, V3> extends AbstractTuple2<V1, V2> implements Tuple3<V1, V2, V3> {

	// FIELDS
	@Nullable
	private final V3 value3;

	// CONSTRUCTOR
	protected AbstractTuple3(@Nullable final V1 value1, @Nullable final V2 value2, @Nullable final V3 value3) {
		super(value1, value2);
		this.value3 = value3;
	}

	// METHODS

	@Override
	protected ImmutableList.Builder<Object> listBuilder() {
		return super.listBuilder().add(this.value3);
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().addValue(this.value3);
	}

	@Override
	public V3 v3() {
		return this.value3;
	}
}
