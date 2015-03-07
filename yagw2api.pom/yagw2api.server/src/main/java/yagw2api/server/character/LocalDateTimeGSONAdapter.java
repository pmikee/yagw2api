package yagw2api.server.character;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * yagw2api.server
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
