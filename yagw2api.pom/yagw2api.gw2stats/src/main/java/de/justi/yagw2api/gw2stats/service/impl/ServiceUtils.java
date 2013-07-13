package de.justi.yagw2api.gw2stats.service.impl;

import com.sun.jersey.api.client.Client;

import de.justi.yagwapi.common.utils.JerseyClientHelper;

final class ServiceUtils {
	public static final int REST_RETRY_COUNT = 10;
	public static final String API_VERSION = "v1";
	public static final Client REST_CLIENT = JerseyClientHelper.createDefaultClient();
}
