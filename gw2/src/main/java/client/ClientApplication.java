package client;

import static com.google.common.base.Preconditions.checkState;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import synchronizer.APISynchronizerDeamon;

import api.APIModule;
import api.service.IWVWService;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class ClientApplication {
	private static final Injector INJECTOR = Guice.createInjector(new ClientModule(), new APIModule());

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		final ClientApplication application = INJECTOR.getInstance(ClientApplication.class);
		application.start();
	}

	@Inject
	private IWVWService wvwService;

	public void start() {
		checkState(this.wvwService != null);
		
		
		APISynchronizerDeamon deamon = new APISynchronizerDeamon(this.wvwService);
		deamon.startAndWait();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
