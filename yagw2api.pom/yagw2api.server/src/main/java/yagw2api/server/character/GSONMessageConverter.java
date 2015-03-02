package yagw2api.server.character;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public final class GSONMessageConverter extends AbstractHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {
	public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;
	private final Gson gson;
	private Type type = null;

	/**
	 * Construct a new {@code GsonMessageConverter}.
	 *
	 * @param gson
	 *            a customized {@link Gson#Gson() Gson}
	 */
	public GSONMessageConverter(final Gson gson) {
		super(new MediaType("application", "json", DEFAULT_CHARSET));
		this.gson = checkNotNull(gson);
	}

	public void setType(final Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	@Override
	public boolean canRead(final Class<?> clazz, final MediaType mediaType) {
		return this.canRead(mediaType);
	}

	@Override
	public boolean canRead(final Type type, final Class<?> contextClass, final MediaType mediaType) {
		return this.canRead(mediaType);
	}

	@Override
	public boolean canWrite(final Class<?> clazz, final MediaType mediaType) {
		return this.canWrite(mediaType);
	}

	@Override
	protected boolean supports(final Class<?> clazz) {
		// should not be called, since we override canRead/Write instead
		throw new UnsupportedOperationException();
	}

	/**
	 * Generic types will be processed here.
	 */
	@Override
	public synchronized Object read(final Type type, final Class<?> contextClass, final HttpInputMessage inputMessage) throws HttpMessageNotReadableException, IOException {
		this.setType(type);
		return this.readInternal(null, inputMessage);
	}

	@Override
	protected Object readInternal(final Class<?> clazz, final HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		final Reader json = new InputStreamReader(inputMessage.getBody(), this.getCharset(inputMessage.getHeaders()));
		try {
			final Type typeOfT = this.getType();
			if (typeOfT != null) {
				return this.gson.fromJson(json, typeOfT);
			} else {
				return this.gson.fromJson(json, clazz);
			}
		} catch (JsonSyntaxException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		} catch (JsonIOException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		} catch (JsonParseException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		} finally {
			this.setType(null);
		}
	}

	@Override
	protected void writeInternal(final Object o, final HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		final OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), this.getCharset(outputMessage.getHeaders()));

		try {
			Type typeOfSrc = this.getType();
			if (typeOfSrc != null) {
				this.gson.toJson(o, typeOfSrc, writer);
			} else {
				this.gson.toJson(o, writer);
			}
			writer.close();
		} catch (JsonIOException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	// helpers

	private Charset getCharset(final HttpHeaders headers) {
		if (headers != null && headers.getContentType() != null && headers.getContentType().getCharSet() != null) {
			return headers.getContentType().getCharSet();
		}
		return DEFAULT_CHARSET;
	}

}