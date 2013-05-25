package model.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.IGuild;
import model.IModelFactory;
import model.IWorld;

public class ModelFactory implements IModelFactory {
	public IGuild createGuild(String id) {
		return new Guild(id);
	}

	@Override
	public IWorld createWorld(int id, String name) {
		return new World(id, checkNotNull(name));
	}

}
