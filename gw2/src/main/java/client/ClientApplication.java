package client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import api.APIModule;
import api.service.IWVWService;
import api.service.dto.IWVWMatchDTO;
import api.service.dto.IWVWMatchesDTO;

import com.google.common.base.Objects;
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

		IWVWMatchesDTO matches = this.wvwService.retrieveAllMatches();
		for (IWVWMatchDTO match : matches.getMatches()) {
			this.wvwService.retrieveMatchDetails(match.getId());
		}

	}
}
