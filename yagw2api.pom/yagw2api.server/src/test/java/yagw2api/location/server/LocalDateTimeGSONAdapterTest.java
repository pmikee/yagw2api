package yagw2api.location.server;

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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.Test;

import yagw2api.server.character.LocalDateTimeGSONAdapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class LocalDateTimeGSONAdapterTest {

	@Test
	public void testReadNull() throws IOException {
		// GIVEN
		final StringReader in = new StringReader("[null]");
		final JsonReader reader = new JsonReader(in);
		reader.beginArray();
		final LocalDateTimeGSONAdapter adapterUnderTest = new LocalDateTimeGSONAdapter();

		// WHEN
		final LocalDateTime read = adapterUnderTest.read(reader);

		// THEN
		assertThat(read, is(nullValue()));
	}

	@Test
	public void testReadTimestamp() throws IOException {
		// GIVEN
		final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
		final StringReader in = new StringReader("[" + now.toEpochSecond(ZoneOffset.UTC) + "]");
		final JsonReader reader = new JsonReader(in);
		reader.beginArray();
		final LocalDateTimeGSONAdapter adapterUnderTest = new LocalDateTimeGSONAdapter();

		// WHEN
		final LocalDateTime read = adapterUnderTest.read(reader);

		// THEN
		assertThat(read, is(equalTo(now.minusNanos(now.getNano()))));
	}

	@Test
	public void testWriteNull() throws IOException {
		// GIVEN
		final StringWriter out = new StringWriter();
		final JsonWriter writer = new JsonWriter(out);
		writer.beginArray();
		final LocalDateTimeGSONAdapter adapterUnderTest = new LocalDateTimeGSONAdapter();

		// WHEN
		adapterUnderTest.write(writer, null);
		writer.endArray();

		// THEN
		assertThat(out.getBuffer().toString(), is(equalTo("[null]")));
	}

	@Test
	public void testWriteTimestamp() throws IOException {
		// GIVEN
		final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
		final StringWriter out = new StringWriter();
		final JsonWriter writer = new JsonWriter(out);
		writer.beginArray();
		final LocalDateTimeGSONAdapter adapterUnderTest = new LocalDateTimeGSONAdapter();

		// WHEN
		adapterUnderTest.write(writer, now);
		writer.endArray();

		// THEN
		assertThat(out.getBuffer().toString(), is(equalTo("[" + now.toEpochSecond(ZoneOffset.UTC) + "]")));
	}
}
