package model;

public interface IWorld {
	int getId();
	String getName();
	
	IWorld createImmutableReference();
}
