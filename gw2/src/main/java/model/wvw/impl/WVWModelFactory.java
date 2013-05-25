package model.wvw.impl;

import model.wvw.IWVWMapBuilder;
import model.wvw.IWVWModelFactory;

public class WVWModelFactory implements IWVWModelFactory {

	@Override
	public IWVWMapBuilder createMapBuilder() {
		return new WVWMapBuilder();
	}

}
