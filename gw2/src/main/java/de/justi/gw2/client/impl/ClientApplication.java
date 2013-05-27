package de.justi.gw2.client.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;

import de.justi.gw2.api.service.IWVWService;
import de.justi.gw2.client.IClientApplication;
import de.justi.gw2.client.wvw.WVW;
import de.justi.gw2.model.IModelFactory;
import de.justi.gw2.model.wvw.IWVWModelFactory;

class ClientApplication implements IClientApplication {
	private IWVWService wvwService;
	private IWVWModelFactory wvwModelFactory;
	private IModelFactory modelFactory;



	@Inject
	public ClientApplication(IWVWService wvwService, IWVWModelFactory wvwModelFactory, IModelFactory modelFactory) {
		checkNotNull(wvwService);
		checkNotNull(wvwModelFactory);
		checkNotNull(modelFactory);
		this.wvwService = wvwService;
		this.wvwModelFactory = wvwModelFactory;
		this.modelFactory = modelFactory;
	}

	public void start() {
		final WVW deamon = new WVW();
		deamon.startAndWait();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public IWVWService getWVWService() {
		return this.wvwService;
	}

	public IWVWModelFactory getWVWModelFactory() {
		return this.wvwModelFactory;
	}

	public IModelFactory getModelFactory() {
		return this.modelFactory;
	}

}
