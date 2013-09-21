package de.justi.yagw2api.arenanet.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
 * -------------------------------------------------------------
 * Copyright (C) 2012 - 2013 Julian Stitz
 * -------------------------------------------------------------
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import static com.google.common.base.Preconditions.checkNotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.sun.jersey.api.client.Client;

import de.justi.yagwapi.common.JerseyClientHelper;

final class ServiceUtils {
	public static final int REST_RETRY_COUNT = 10;
	public static final String API_VERSION = "v1";
	public static final Client REST_CLIENT = JerseyClientHelper.createDefaultClient();
	// 2013-06-08T01:00:00Z
	public static final transient DateFormat ZULU_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
	static {
		ZULU_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("Zulu"));
	}

	public static Locale normalizeLocaleForAPIUsage(Locale locale) {
		checkNotNull(locale);
		return Locale.forLanguageTag(locale.getLanguage());
	}
}
