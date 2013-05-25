package client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import utils.InjectionHelper;

public class ClientApplication {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		InjectionHelper.INSTANCE.getInjector().getInstance(IClientApplication.class).start();
	}

}
