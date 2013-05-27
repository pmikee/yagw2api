package de.justi.gw2.model;


public interface IModelFactory {
	IGuild createGuild(String id);
	IWorld createWorld(int id, String name);
}
