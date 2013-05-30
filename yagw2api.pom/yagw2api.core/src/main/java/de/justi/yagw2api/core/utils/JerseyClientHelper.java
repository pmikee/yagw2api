package de.justi.yagw2api.core.utils;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public enum JerseyClientHelper {
	DEFAULT;
	
	public static Client createDefaultClient() {
		return DEFAULT.createClient();
	}

	private static final int	CONNECTION_TIMEOUT_MILLIS	= 30 * 1000;
	private static final int	READ_TIMEOUT_MILLIS			= 30 * 1000;

	private JerseyClientHelper() {

	}

	private ClientConfig configureClient() {
		TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		} };
		SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(null, certs, new SecureRandom());
		} catch (java.security.GeneralSecurityException ex) {
		}
		HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

		ClientConfig config = new DefaultClientConfig();
		try {
			config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			}, ctx));
		} catch (Exception e) {
		}
		return config;
	}

	public Client createClient() {
		final Client restClient = Client.create(this.configureClient());
		restClient.setFollowRedirects(true);
		restClient.setReadTimeout(READ_TIMEOUT_MILLIS);
		restClient.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS);
		return restClient;
	}
}
