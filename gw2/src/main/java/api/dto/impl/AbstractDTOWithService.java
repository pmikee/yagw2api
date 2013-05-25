package api.dto.impl;

import utils.InjectionHelper;
import api.service.IWVWService;

abstract class AbstractDTOWithService {
	private final transient IWVWService service = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWService.class);
	protected final IWVWService getService(){
		return this.service;
	}
}
