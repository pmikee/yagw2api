package de.justi.yagw2api.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import de.justi.yagw2api.api.service.IWVWService;
import de.justi.yagw2api.wrapper.IWVWWrapper;
import de.justi.yagw2api.wrapper.model.AbstractHasChannel;
import de.justi.yagw2api.wrapper.model.IEvent;
import de.justi.yagw2api.wrapper.model.IModelFactory;
import de.justi.yagw2api.wrapper.model.wvw.IWVWModelFactory;

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
		deamon.startAndWait();
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