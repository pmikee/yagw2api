package model;


public interface IModelFactory {
	IGuild createGuild(String id);
	IWorld createWorld(int id, String name);
}
