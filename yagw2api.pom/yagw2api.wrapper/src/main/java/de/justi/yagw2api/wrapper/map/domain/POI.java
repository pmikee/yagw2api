package de.justi.yagw2api.wrapper.map.domain;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
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

import de.justi.yagw2api.common.tuple.DoubleTuple2;
import de.justi.yagw2api.common.tuple.UniformNumberTuple2;

public interface POI {

	public static interface POIBuilder<T extends POI, B extends POIBuilder<T, B>> {

		B id(@Nullable String id);

		B name(@Nullable String name);

		B location(@Nullable UniformNumberTuple2<Double> location);

		T build();
	}

	String getId();

	String getName();

	DoubleTuple2 getLocation();
}
