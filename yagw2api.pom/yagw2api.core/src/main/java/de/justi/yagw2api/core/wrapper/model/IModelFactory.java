package de.justi.yagw2api.core.wrapper.model;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.IWorld.IWorldBuilder;


public interface IModelFactory {
	void clearCache();
	IGuild getOrCreateGuild(String id);
	
	IWorldBuilder newWorldBuilder();
	Optional<IWorld> getWorld(int id);
}
