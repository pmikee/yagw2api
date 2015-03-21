package de.justi.yagw2api.arenanet;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.sun.jersey.api.client.Client;

import de.justi.yagwapi.common.JerseyClientHelper;

public final class ArenanetUtils {
	public static final int REST_RETRY_COUNT = 10;
	public static final String API_VERSION = "v1";
	public static final Client REST_CLIENT = JerseyClientHelper.createDefaultClient();
	// 2013-06-08T01:00:00Z
	private static final transient DateFormat ZULU_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
	static {
		ZULU_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("Zulu"));
	}

	private ArenanetUtils() {
		throw new AssertionError("no instance");
	}

	public static Locale normalizeLocaleForAPIUsage(final Locale locale) {
		checkNotNull(locale);
		return Locale.forLanguageTag(locale.getLanguage());
	}

	public static final LocalDateTime parseZULUTimestampString(final String timestampString) throws ParseException {
		Date ts = ZULU_DATE_FORMAT.parse(timestampString);
		final Instant instant = Instant.ofEpochMilli(ts.getTime());
		final LocalDateTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return res;
	}
}
