package de.justi.yagw2api.wrapper;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.IWorld.IWorldBuilder;


public interface IModelFactory {
	void clearCache();
	IGuild getOrCreateGuild(String id, String name, String tag);
	
	IWorldBuilder newWorldBuilder();
	Optional<IWorld> getWorld(int id);
}
