package de.justi.yagw2api.core.wrapper.model;


public interface IModelFactory {
	IGuild getOrCreateGuild(String id);
	IWorld getOrCreateWorld(int id, String name);
}
