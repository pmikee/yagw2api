package de.justi.gw2.api.service.impl;


import com.sun.jersey.api.client.Client;

import de.justi.gw2.utils.JerseyClientHelper;

public class AbstractService {
	protected static final Client CLIENT = JerseyClientHelper.INSTANCE.createClient();
	static{
		CLIENT.setFollowRedirects(true);
	}

}
