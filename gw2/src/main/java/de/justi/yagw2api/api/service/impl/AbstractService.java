package de.justi.yagw2api.api.service.impl;

import com.sun.jersey.api.client.Client;

import de.justi.yagw2api.utils.JerseyClientHelper;

public class AbstractService {
	protected static final Client	CLIENT						= JerseyClientHelper.INSTANCE.createClient();
}
