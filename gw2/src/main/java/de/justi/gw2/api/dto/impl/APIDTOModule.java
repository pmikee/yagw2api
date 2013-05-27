package de.justi.gw2.api.dto.impl;


import com.google.inject.AbstractModule;

import de.justi.gw2.api.dto.IWVWDTOFactory;

public class APIDTOModule extends AbstractModule {

	@Override
	protected void configure() {		
		// factories
		this.bind(IWVWDTOFactory.class).to(WVWDTOFactory.class).asEagerSingleton();		
	}

}
