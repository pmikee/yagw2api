package client.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.IModelFactory;
import model.wvw.IWVWMap;
import model.wvw.IWVWModelFactory;
import api.dto.IWVWMatchDTO;
import api.dto.IWVWMatchDetailsDTO;
import api.service.IWVWService;
import client.IClientApplication;

import com.google.inject.Inject;

class ClientApplication implements IClientApplication {
	private IWVWService wvwService;
	private IWVWModelFactory wvwModelFactory;
	private IModelFactory modelFactory;

	private IWVWMap centerMap = null;
	private IWVWMap redMap = null;
	private IWVWMap greenMap = null;
	private IWVWMap blueMap = null;

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
		final IWVWMatchDTO match = this.wvwService.retrieveAllMatches().getMatches()[0];
		System.out.println(match);
		final IWVWMatchDetailsDTO matchDetails = match.getDetails().get();
		System.out.println(matchDetails);
		
		this.centerMap = this.getWVWModelFactory().createMapBuilder().fromDTO(matchDetails.getCenterMap()).build();
		this.redMap = this.getWVWModelFactory().createMapBuilder().fromDTO(matchDetails.getRedMap()).build();
		this.greenMap = this.getWVWModelFactory().createMapBuilder().fromDTO(matchDetails.getGreenMap()).build();
		this.blueMap = this.getWVWModelFactory().createMapBuilder().fromDTO(matchDetails.getBlueMap()).build();
		System.out.println();
		System.out.println(this.centerMap);
		System.out.println(this.redMap);
		System.out.println(this.greenMap);
		System.out.println(this.blueMap);

		// final APISynchronizerDeamon deamon = new
		// APISynchronizerDeamon(this.wvwService);
		// deamon.startAndWait();
		// try {
		// Thread.sleep(10000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
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
