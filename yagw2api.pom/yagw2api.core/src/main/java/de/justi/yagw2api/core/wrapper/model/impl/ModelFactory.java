package de.justi.yagw2api.core.wrapper.model.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Map;

import de.justi.yagw2api.core.wrapper.model.IGuild;
import de.justi.yagw2api.core.wrapper.model.IModelFactory;
import de.justi.yagw2api.core.wrapper.model.IWorld;

class ModelFactory implements IModelFactory {
	private final Map<String, IGuild> guildsMappedById = new HashMap<String, IGuild>();
	private final Map<Integer, IWorld> worldsMappedById = new HashMap<Integer, IWorld>();
	
	public IGuild getOrCreateGuild(String id) {
		checkNotNull(id);
		if(!this.guildsMappedById.containsKey(id)) {
			synchronized (this) {
				if(!this.guildsMappedById.containsKey(id)) {
					this.guildsMappedById.put(id, new Guild(id));
				}
			}
		}
		checkState(this.guildsMappedById.containsKey(id));
		return this.guildsMappedById.get(id);
	}

	@Override
	public IWorld getOrCreateWorld(int id, String name) {
		checkArgument(id > 0);
		checkNotNull(name);
		if(!this.worldsMappedById.containsKey(id)) {
			synchronized (this) {
				if(!this.worldsMappedById.containsKey(id)) {
					this.worldsMappedById.put(id, new World(id, name));
				}
			}
		}
		checkState(this.worldsMappedById.containsKey(id));
		final IWorld world = this.worldsMappedById.get(id);
		world.setName(name);
		return world;
	}

}
