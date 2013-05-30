package de.justi.yagw2api.core.wrapper.model;

public interface IWorld {
	int getId();
	String getName();
	void setName(String name);
	
	IWorld createUnmodifiableReference();
}
