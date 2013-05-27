package de.justi.gw2.client;

import de.justi.gw2.api.service.IWVWService;
import de.justi.gw2.model.IModelFactory;
import de.justi.gw2.model.wvw.IWVWModelFactory;

public interface IClientApplication {
	void start();
	IWVWService getWVWService();
	IWVWModelFactory getWVWModelFactory();
	IModelFactory getModelFactory();
}
