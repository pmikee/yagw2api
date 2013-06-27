package de.justi.yagw2api.core.wrapper.model.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.IGuild;
import de.justi.yagw2api.core.wrapper.model.IModelFactory;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.IWorld.IWorldBuilder;

final class ModelFactory implements IModelFactory {
	private static final Logger LOGGER = Logger.getLogger(ModelFactory.class);
	private final Map<String, IGuild> guildsMappedById = new HashMap<String, IGuild>();
	private final Map<Integer, IWorld> worldsMappedById = new HashMap<Integer, IWorld>();
	
	public IGuild getOrCreateGuild(String id, String name, String tag) {
		checkNotNull(id);
		if(!this.guildsMappedById.containsKey(id)) {
			synchronized (this) {
				if(!this.guildsMappedById.containsKey(id)) {
					this.guildsMappedById.put(id, new Guild(id, name, tag));
				}
			}
		}
		checkState(this.guildsMappedById.containsKey(id));
		return this.guildsMappedById.get(id);
	}

	@Override
	public Optional<IWorld> getWorld(int id) {
		checkArgument(id > 0);
		return Optional.fromNullable(this.worldsMappedById.get(id));
	}

	@Override
	public IWorldBuilder newWorldBuilder() {
		return new World.WorldBuilder();
	}

	@Override
	public void clearCache() {
		LOGGER.warn("Going to clear cache of "+this.toString());
		this.guildsMappedById.clear();
		this.worldsMappedById.clear();
	}

}
