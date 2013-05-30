package de.justi.yagw2api.core.api.service.impl;

import com.sun.jersey.api.client.Client;

import de.justi.yagw2api.core.utils.JerseyClientHelper;

class AbstractService {
	protected static final Client CLIENT = JerseyClientHelper.createDefaultClient();
}
