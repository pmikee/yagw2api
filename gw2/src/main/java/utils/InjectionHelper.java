package utils;

import model.impl.ModelModule;
import model.wvw.events.impl.WVWModelEventsModule;
import model.wvw.impl.WVWModelModule;
import api.dto.impl.APIDTOModule;
import api.service.impl.APIServiceModule;
import client.impl.ClientModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public enum InjectionHelper {
	INSTANCE;
	
	private final Injector injector = Guice.createInjector(new ClientModule(), new APIServiceModule(), new APIDTOModule(), new ModelModule(), new WVWModelModule(), new WVWModelEventsModule());
	public Injector getInjector() {
		return this.injector;
	}
}
