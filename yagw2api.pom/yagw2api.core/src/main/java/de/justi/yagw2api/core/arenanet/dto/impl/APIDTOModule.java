package de.justi.yagw2api.core.arenanet.dto.impl;


import com.google.inject.AbstractModule;

import de.justi.yagw2api.core.arenanet.dto.IWVWDTOFactory;

public class APIDTOModule extends AbstractModule {

	@Override
	protected void configure() {		
		// factories
		this.bind(IWVWDTOFactory.class).to(WVWDTOFactory.class).asEagerSingleton();		
	}

}
