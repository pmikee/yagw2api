package model;

import model.wvw.IWVWModelFactory;

public interface IModelFactory {
	IWVWModelFactory getWVWModelFactory();
	IGuild createGuild(String id);
}
