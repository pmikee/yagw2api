package de.justi.yagw2api.analyzer.entities;

import java.util.Collection;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.IWorld;

public interface IWorldEnityDAO {
	Collection<IWorldEntity> retrieveAllWorldEntities();

	Optional<IWorldEntity> findWorldEntityById(int id);
	Optional<IWorldEntity> findWorldEntityByOriginId(int originId);

	Optional<IWorldEntity> findWorldEntityByName(String name);

	Collection<IWorldEntity> searchWorldEntityByNamePart(String part);

	/**
	 * <p>
	 * try to find the {@link IWorldEntity} that belongs to the given
	 * {@link IWorld}
	 * </p>
	 * 
	 * @param world
	 * @return {@link Optional#absent()} if there is no such
	 *         {@link IWorldEntity}, else an {@link Optional} of that
	 *         {@link IWorldEntity} is returned
	 */
	Optional<IWorldEntity> findWorldEntityOf(IWorld world);

	/**
	 * <p>
	 * returns the {@link IWorldEntity} that belongs to the given {@link IWorld}
	 * </p>
	 * <p>
	 * if there is no such {@link IWorldEntity} a new will be created
	 * </p>
	 * 
	 * @param world
	 * @return
	 */
	IWorldEntity findOrCreateWorldEntityOf(IWorld world);

	/**
	 * <p>
	 * try to create a new {@link IWorldEntity} for the given {@link IWorld}
	 * </p>
	 * 
	 * @param world
	 * @return {@link Optional#absent()} if unable to create a new
	 *         {@link IWorldEntity} (that might caused by an already existing
	 *         {@link IWorldEntity} for the given {@link IWorld}), otherwise an
	 *         {@link Optional} of the new {@link IWorldEntity} will be returned
	 */
	Optional<IWorldEntity> newWorldEntityOf(IWorld world);
}
