package de.justi.gw2.api.service.impl;

import com.sun.jersey.api.client.Client;

import de.justi.gw2.utils.JerseyClientHelper;

public class AbstractService {
	protected static final Client	CLIENT						= JerseyClientHelper.INSTANCE.createClient();
	private static final int		CONNECTION_TIMEOUT_MILLIS	= 30 * 1000;
	static {
		CLIENT.setFollowRedirects(true);
		CLIENT.setReadTimeout(CONNECTION_TIMEOUT_MILLIS);
	}

}
