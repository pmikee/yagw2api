package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

abstract class AbstractGSONFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGSONFactory.class);
	private static final Gson DEFAULT_GSON = new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setVersion(1.0).create();

	// FIELDS
	private final Gson gson;

	// CONSTRUCTOR
	protected AbstractGSONFactory() {
		this(DEFAULT_GSON);
	}

	protected AbstractGSONFactory(final Gson gson) {
		this.gson = checkNotNull(gson, "missing gson");
	}

	protected final Gson getGSON() {
		return this.gson;
	}

}
