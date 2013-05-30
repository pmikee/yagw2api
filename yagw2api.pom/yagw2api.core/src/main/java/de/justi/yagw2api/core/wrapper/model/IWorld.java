package de.justi.yagw2api.core.wrapper.model;

public interface IWorld {
	int getId();
	String getName();
	
	IWorld createImmutableReference();
}
