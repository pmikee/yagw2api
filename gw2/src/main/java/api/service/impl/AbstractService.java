package api.service.impl;

import utils.JerseyClientHelper;

import com.sun.jersey.api.client.Client;

public class AbstractService {
	protected static final Client CLIENT = JerseyClientHelper.INSTANCE.createClient();
	static{
		CLIENT.setFollowRedirects(true);
	}

}
