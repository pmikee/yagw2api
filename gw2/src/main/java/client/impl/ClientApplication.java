package client.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.IModelFactory;
import model.wvw.IWVWModelFactory;
import synchronizer.APISynchronizerDeamon;
import api.service.IWVWService;
import client.IClientApplication;

import com.google.inject.Inject;

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
		final APISynchronizerDeamon deamon = new APISynchronizerDeamon(this.wvwService);
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
