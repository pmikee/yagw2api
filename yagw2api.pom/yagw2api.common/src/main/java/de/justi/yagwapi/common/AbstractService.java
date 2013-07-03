package de.justi.yagwapi.common;

import com.sun.jersey.api.client.Client;

import de.justi.yagwapi.common.utils.JerseyClientHelper;

public class AbstractService {
	protected static final Client CLIENT = JerseyClientHelper.createDefaultClient();
}
