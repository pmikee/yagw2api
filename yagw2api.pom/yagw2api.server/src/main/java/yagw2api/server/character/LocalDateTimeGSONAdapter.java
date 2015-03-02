package yagw2api.server.character;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public final class LocalDateTimeGSONAdapter extends TypeAdapter<LocalDateTime> {

	@Override
	public void write(final JsonWriter writer, final LocalDateTime value) throws IOException {
		if (value == null) {
			writer.nullValue();
		} else {
			writer.value(value.toEpochSecond(ZoneOffset.UTC));
		}
	}

	@Override
	public LocalDateTime read(final JsonReader reader) throws IOException {
		if (reader.peek() == JsonToken.NULL) {
			reader.nextNull();
			return null;
		} else {
			return LocalDateTime.ofEpochSecond(reader.nextLong(), 0, ZoneOffset.UTC);
		}
	}
}
