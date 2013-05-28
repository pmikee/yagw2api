package de.justi.yagw2api.wrapper;

import de.justi.yagw2api.api.service.IWVWService;
import de.justi.yagw2api.wrapper.model.IModelFactory;
import de.justi.yagw2api.wrapper.model.wvw.IWVWModelFactory;

public interface IWVW {
	void start();
	IWVWService getWVWService();
	IWVWModelFactory getWVWModelFactory();
	IModelFactory getModelFactory();
}
