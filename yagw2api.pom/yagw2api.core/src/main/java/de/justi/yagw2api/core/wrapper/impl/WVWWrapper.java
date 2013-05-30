package de.justi.yagw2api.core.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import de.justi.yagw2api.core.arenanet.service.IWVWService;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.AbstractHasChannel;
import de.justi.yagw2api.core.wrapper.model.IEvent;
import de.justi.yagw2api.core.wrapper.model.IModelFactory;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWModelFactory;

class WVWWrapper extends AbstractHasChannel implements IWVWWrapper {
	private IWVWService wvwService;
	private IWVWModelFactory wvwModelFactory;
	private IModelFactory modelFactory;



	@Inject
	public WVWWrapper(IWVWService wvwService, IWVWModelFactory wvwModelFactory, IModelFactory modelFactory) {
		checkNotNull(wvwService);
		checkNotNull(wvwModelFactory);
		checkNotNull(modelFactory);
		this.wvwService = wvwService;
		this.wvwModelFactory = wvwModelFactory;
		this.modelFactory = modelFactory;
	}

	public void start() {
		final WVWSynchronizer deamon = new WVWSynchronizer();
		deamon.getChannel().register(this);
		deamon.start();
	}
	
	@Subscribe
	public void onEvent(IEvent event) {
		this.getChannel().post(event);
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
