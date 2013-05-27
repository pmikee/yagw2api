package de.justi.gw2.model.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.gw2.model.IGuild;
import de.justi.gw2.model.IModelFactory;
import de.justi.gw2.model.IWorld;

public class ModelFactory implements IModelFactory {
	public IGuild createGuild(String id) {
		return new Guild(id);
	}

	@Override
	public IWorld createWorld(int id, String name) {
		return new World(id, checkNotNull(name));
	}

}
