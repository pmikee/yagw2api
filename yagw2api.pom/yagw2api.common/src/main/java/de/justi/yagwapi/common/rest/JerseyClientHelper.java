package de.justi.yagwapi.common.rest;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Commons
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public enum JerseyClientHelper {
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
