package de.justi.yagw2api.arenanet.service.impl;

import com.sun.jersey.api.client.Client;

import de.justi.yagwapi.common.utils.JerseyClientHelper;

class AbstractService {
	protected static final Client CLIENT = JerseyClientHelper.createDefaultClient();
}
