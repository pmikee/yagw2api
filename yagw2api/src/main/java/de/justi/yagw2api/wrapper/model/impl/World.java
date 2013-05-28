package de.justi.yagw2api.wrapper.model.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

import de.justi.yagw2api.wrapper.model.IWorld;


public class World implements IWorld {
	private final int id;	
	private String name;
	
	public World(int id, String name) {
		checkArgument(id > 0);
		checkNotNull(name);
		this.id = id;
		this.name = name;
	}
	
	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("name", this.name).toString();
	}

	@Override
	public IWorld createImmutableReference() {
		return this;
	}
}
