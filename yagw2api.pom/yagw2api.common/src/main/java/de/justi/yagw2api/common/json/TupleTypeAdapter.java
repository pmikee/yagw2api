package de.justi.yagw2api.common.json;

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
		return Tuples.of(tupleParts.build());
	}
}