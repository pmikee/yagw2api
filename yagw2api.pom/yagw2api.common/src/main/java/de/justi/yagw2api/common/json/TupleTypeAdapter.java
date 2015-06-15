package de.justi.yagw2api.common.json;

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

import java.lang.reflect.Type;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.justi.yagw2api.common.tuple.Tuple;
import de.justi.yagw2api.common.tuple.Tuples;

public final class TupleTypeAdapter implements JsonSerializer<Tuple>, JsonDeserializer<Tuple> {

	@Override
	public JsonElement serialize(final Tuple src, final Type typeOfSrc, final JsonSerializationContext context) {
		final JsonArray jsonArray = new JsonArray();
		for (Object tuplePart : src.asList()) {
			jsonArray.add(context.serialize(tuplePart));
		}
		return jsonArray;
	}

	@Override
	public Tuple deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		if (!(json instanceof JsonArray)) {
			throw new JsonParseException("The tuple should be a json array but was " + json.getClass());
		}
		final JsonArray jsonArray = (JsonArray) json;
		final ImmutableList.Builder<?> tupleParts = ImmutableList.builder();
		for (JsonElement element : jsonArray) {
			tupleParts.add(context.deserialize(element, Object.class));
		}
		return Tuples.from(tupleParts.build());
	}
}