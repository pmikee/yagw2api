package de.justi.gw2.model;

public interface IWorld {
	int getId();
	String getName();
	
	IWorld createImmutableReference();
}
