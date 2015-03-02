package yagw2api.server.character;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.gson.GsonBuilder;

@ComponentScan
@EnableAutoConfiguration
public final class CharacterServer extends WebMvcConfigurerAdapter implements Closeable {

	// STATICS
	public static final void main(final String[] args) {
		startNew();
	}

	public static final CharacterServer startNew() {
		ConfigurableApplicationContext ctx = SpringApplication.run(CharacterServer.class);
		return new CharacterServer(ctx);
	}

	// FIELDS
	private final ConfigurableApplicationContext ctx;
	private final AtomicBoolean closed = new AtomicBoolean(false);

	// CONSTRUCTOR
	@Autowired
	public CharacterServer(final ConfigurableApplicationContext ctx) {
		this.ctx = checkNotNull(ctx, "missing ctx");
	}

	// METHODS

	@Override
	public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
		checkNotNull(converters, "missing converters");
		converters.clear();
		converters.add(new GSONMessageConverter(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()));
	}

	public boolean isClosed() {
		return this.closed.get();
	}

	@Override
	public void close() throws IOException {
		if (!this.closed.getAndSet(true)) {
			try {
				final int exitCode = SpringApplication.exit(this.ctx);
				if (exitCode != 0) {
					throw new IOException("Application did not shutdown cleanly. Exit code: " + exitCode);
				}
			} catch (Exception e) {
				if (e instanceof IOException) {
					throw e;
				} else {
					throw new IOException("Failed to shutdown", e);
				}
			}
		} else {
			throw new IllegalAccessError(this + " is already closed");
		}
	}
}
