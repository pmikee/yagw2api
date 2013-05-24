package api;

import api.service.IWVWService;
import api.service.dto.IWVWDTOFactory;
import api.service.dto.impl.WVWDTOFactory;
import api.service.impl.WVWService;

import com.google.inject.AbstractModule;

public class APIModule extends AbstractModule {

	@Override
	protected void configure() {
		// services
		this.bind(IWVWService.class).to(WVWService.class);
		
		// factories
		this.bind(IWVWDTOFactory.class).to(WVWDTOFactory.class);		
	}

}
