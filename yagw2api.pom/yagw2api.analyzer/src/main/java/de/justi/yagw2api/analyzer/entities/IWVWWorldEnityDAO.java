package de.justi.yagw2api.analyzer.entities;

import java.util.Collection;

import com.google.common.base.Optional;

public interface IWVWWorldEnityDAO {
	Collection<IWVWWorldEntity> retrieveAllWorldEntities();
	Optional<IWVWWorldEntity> findWorldEntityById(int id);
	Optional<IWVWWorldEntity> findWorldEntityByName(String name);
	Collection<IWVWWorldEntity> searchWorldEntityByNamePart(String part);
	
	Optional<IWVWWorldEntity> newWorldEntity(String name);
}
