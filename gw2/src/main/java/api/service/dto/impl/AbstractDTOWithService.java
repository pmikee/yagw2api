package api.service.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import api.service.IWVWService;

abstract class AbstractDTOWithService {
	private final transient IWVWService service;
	public AbstractDTOWithService(IWVWService service) {
		this.service = checkNotNull(service);
	}
	protected final IWVWService getService(){
		return this.service;
	}
}
