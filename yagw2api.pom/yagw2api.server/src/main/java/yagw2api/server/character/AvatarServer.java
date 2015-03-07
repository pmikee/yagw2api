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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.gson.GsonBuilder;

@ComponentScan
@EnableAutoConfiguration
public final class AvatarServer extends WebMvcConfigurerAdapter implements Closeable {

	// STATICS
	public static final void main(final String[] args) {
		startNew();
	}

	public static final AvatarServer startNew() {
		ConfigurableApplicationContext ctx = SpringApplication.run(AvatarServer.class);
		return new AvatarServer(ctx);
	}

	// FIELDS
	private final ConfigurableApplicationContext ctx;
	private final AtomicBoolean closed = new AtomicBoolean(false);

	// CONSTRUCTOR
	@Autowired
	public AvatarServer(final ConfigurableApplicationContext ctx) {
		this.ctx = checkNotNull(ctx, "missing ctx");
	}

	// METHODS

	@Override
	public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
		checkNotNull(converters, "missing converters");
		converters.clear();
		final GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
		gsonConverter.setGson(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGSONAdapter()).create());
		converters.add(gsonConverter);
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
