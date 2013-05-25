package api.dto.impl;

import api.dto.IWVWDTOFactory;

import com.google.inject.AbstractModule;

public class APIDTOModule extends AbstractModule {

	@Override
	protected void configure() {		
		// factories
		this.bind(IWVWDTOFactory.class).to(WVWDTOFactory.class).asEagerSingleton();		
	}

}
