package de.justi.gw2.utils;


import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.gw2.api.dto.impl.APIDTOModule;
import de.justi.gw2.api.service.impl.APIServiceModule;
import de.justi.gw2.client.impl.ClientModule;
import de.justi.gw2.model.impl.ModelModule;
import de.justi.gw2.model.wvw.events.impl.WVWModelEventsModule;
import de.justi.gw2.model.wvw.impl.WVWModelModule;

public enum InjectionHelper {
	INSTANCE;
	
	private final Injector injector = Guice.createInjector(new ClientModule(), new APIServiceModule(), new APIDTOModule(), new ModelModule(), new WVWModelModule(), new WVWModelEventsModule());
	public Injector getInjector() {
		return this.injector;
	}
}
