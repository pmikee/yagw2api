package client;

import api.service.IWVWService;
import model.IModelFactory;
import model.wvw.IWVWModelFactory;

public interface IClientApplication {
	void start();
	IWVWService getWVWService();
	IWVWModelFactory getWVWModelFactory();
	IModelFactory getModelFactory();
}
