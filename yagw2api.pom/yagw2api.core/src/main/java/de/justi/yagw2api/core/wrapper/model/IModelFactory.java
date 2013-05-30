package de.justi.yagw2api.core.wrapper.model;


public interface IModelFactory {
	IGuild createGuild(String id);
	IWorld createWorld(int id, String name);
}
