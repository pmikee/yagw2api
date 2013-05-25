package model.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

import model.IWorld;

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

	@Override
	public void setName(String name) {
		checkNotNull(name);
		this.name = name;
	}
	
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("name", this.name).toString();
	}
}
