package de.justi.yagw2api.core.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.core.api.dto.impl.APIDTOModule;
import de.justi.yagw2api.core.api.service.impl.APIServiceModule;
import de.justi.yagw2api.core.wrapper.impl.WrapperModule;
import de.justi.yagw2api.core.wrapper.model.impl.ModelModule;
import de.justi.yagw2api.core.wrapper.model.wvw.events.impl.WVWModelEventsModule;
import de.justi.yagw2api.core.wrapper.model.wvw.impl.WVWModelModule;

public enum YAGW2APIInjectionHelper {
	INSTANCE;

	public static Injector getInjector() {
		return INSTANCE.injector;
	}

	private final Injector injector = Guice.createInjector(new WrapperModule(), new APIServiceModule(), new APIDTOModule(), new ModelModule(),
			new WVWModelModule(), new WVWModelEventsModule());
}
