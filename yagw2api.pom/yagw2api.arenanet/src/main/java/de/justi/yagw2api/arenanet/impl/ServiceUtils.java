package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.sun.jersey.api.client.Client;

import de.justi.yagwapi.common.utils.JerseyClientHelper;

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
