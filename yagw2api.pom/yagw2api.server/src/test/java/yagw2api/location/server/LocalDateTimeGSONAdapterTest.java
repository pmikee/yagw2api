package yagw2api.location.server;

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
