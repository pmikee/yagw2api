package de.justi.yagw2api.core.arenanet.service.impl;

import com.sun.jersey.api.client.Client;

class AbstractService {
	protected static final Client CLIENT = JerseyClientHelper.createDefaultClient();
}
