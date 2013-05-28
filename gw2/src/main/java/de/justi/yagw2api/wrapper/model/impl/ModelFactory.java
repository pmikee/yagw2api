package de.justi.yagw2api.wrapper.model.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import de.justi.yagw2api.wrapper.model.IGuild;
import de.justi.yagw2api.wrapper.model.IModelFactory;
import de.justi.yagw2api.wrapper.model.IWorld;

public class ModelFactory implements IModelFactory {
	public IGuild createGuild(String id) {
		return new Guild(id);
	}

	@Override
	public IWorld createWorld(int id, String name) {
		return new World(id, checkNotNull(name));
	}

}
