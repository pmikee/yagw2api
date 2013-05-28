package de.justi.yagw2api.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;

import de.justi.yagw2api.api.service.IWVWService;
import de.justi.yagw2api.wrapper.IWVW;
import de.justi.yagw2api.wrapper.model.IModelFactory;
import de.justi.yagw2api.wrapper.model.wvw.IWVWModelFactory;
import de.justi.yagw2api.wrapper.wvw.WVW;

class ClientApplication implements IWVW {
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
