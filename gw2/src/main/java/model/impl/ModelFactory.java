package model.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.IGuild;
import model.IModelFactory;
import model.wvw.IWVWModelFactory;

import com.google.inject.Inject;

public class ModelFactory implements IModelFactory {	
	private IWVWModelFactory wvwModelFactory;
	
	@Inject
	public ModelFactory(IWVWModelFactory wvwModelFactory) {
		this.wvwModelFactory = checkNotNull(wvwModelFactory);		
	}
	
	public IWVWModelFactory getWVWModelFactory() {
		return this.wvwModelFactory;
	}

	public IGuild createGuild(String id) {
		return new Guild(id);
	}

}
