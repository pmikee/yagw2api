package de.justi.yagw2api.utils;


import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.api.dto.impl.APIDTOModule;
import de.justi.yagw2api.api.service.impl.APIServiceModule;
import de.justi.yagw2api.wrapper.impl.WrapperModule;
import de.justi.yagw2api.wrapper.model.impl.ModelModule;
import de.justi.yagw2api.wrapper.model.wvw.events.impl.WVWModelEventsModule;
import de.justi.yagw2api.wrapper.model.wvw.impl.WVWModelModule;

public enum InjectionHelper {
	INSTANCE;
	
	private final Injector injector = Guice.createInjector(new WrapperModule(), new APIServiceModule(), new APIDTOModule(), new ModelModule(), new WVWModelModule(), new WVWModelEventsModule());
	public Injector getInjector() {
		return this.injector;
	}
}
