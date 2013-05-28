package de.justi.yagw2api.wrapper.model;

public interface IWorld {
	int getId();
	String getName();
	
	IWorld createImmutableReference();
}
