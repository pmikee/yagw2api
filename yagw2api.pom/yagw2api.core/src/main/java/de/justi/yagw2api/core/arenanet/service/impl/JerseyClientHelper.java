package de.justi.yagw2api.core.arenanet.service.impl;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

enum JerseyClientHelper {
	DEFAULT;

	public static Client createDefaultClient() {
		return DEFAULT.createClient();
	}

	private static final int CONNECTION_TIMEOUT_MILLIS = 30 * 1000;
	private static final int READ_TIMEOUT_MILLIS = 30 * 1000;

	private JerseyClientHelper() {

	}

	private ClientConfig createClientConfig() {
		HttpsURLConnection.setDefaultSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
		final ClientConfig config = new DefaultClientConfig();
		return config;
	}

	public Client createClient() {
		final Client restClient = Client.create(this.createClientConfig());
		restClient.setFollowRedirects(true);
		restClient.setReadTimeout(READ_TIMEOUT_MILLIS);
		restClient.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS);
		return restClient;
	}
}
