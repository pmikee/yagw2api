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

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableList;

class AbstractTuple5<V1, V2, V3, V4, V5> extends AbstractTuple4<V1, V2, V3, V4> implements Tuple5<V1, V2, V3, V4, V5> {

	// FIELDS
	@Nullable
	private final V5 value5;

	// CONSTRUCTOR
	protected AbstractTuple5(@Nullable final V1 value1, @Nullable final V2 value2, @Nullable final V3 value3, @Nullable final V4 value4, @Nullable final V5 value5) {
		super(value1, value2, value3, value4);
		this.value5 = value5;
	}

	// METHODS

	@Override
	protected ImmutableList.Builder<Object> listBuilder() {
		return super.listBuilder().add(this.value5);
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("value5", this.value5);
	}

	@Override
	public V5 v5() {
		return this.value5;
	}

}
