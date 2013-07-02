package de.justi.yagw2api.analyzer.entities;

import java.util.Collection;
import java.util.Locale;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.model.IWorld;

public interface IWorldEnityDAO {
	/**
	 * <p>
	 * Retrieve all persisted {@link IWorldEntity}s
	 * </p>
	 * 
	 * @return {@link Collection} of all persisted {@link IWorldEntity}s, that
	 *         might be empty if no {@link IWorldEntity} has been persisted yet
	 */
	Collection<IWorldEntity> retrieveAllWorldEntities();

	/**
	 * <p>
	 * fined a {@link IWorldEntity} by its primary key ({@link IEntity#getId()}
	 * </p>
	 * 
	 * @param id
	 *            primary key of the {@link IWorldEntity}
	 * @return {@link Optional#absent()} if there was no matching
	 *         {@link IWorldEntity}, else {@link Optional#or(Object)} of
	 *         matching {@link IWorldEntity} is returned
	 */
	Optional<IWorldEntity> findWorldEntityById(long id);

	/**
	 * <p>
	 * fined a {@link IWorldEntity} by its origin id
	 * {@link IWorldEntity#getOriginWorldId()}
	 * </p>
	 * 
	 * @param originId
	 *            see {@link IWorld#getId()}
	 * @return {@link Optional#absent()} if there was no matching
	 *         {@link IWorldEntity}, else {@link Optional#or(Object)} of
	 *         matching {@link IWorldEntity} is returned
	 */
	Optional<IWorldEntity> findWorldEntityByOriginId(int originId);

	/**
	 * <p>
	 * find a specific {@link IWorldEntity} identified by its name in the given
	 * language
	 * </p>
	 * 
	 * @param locale
	 * @param name
	 * @return {@link Optional#absent()} if there was no matching
	 *         {@link IWorldEntity}, else {@link Optional#or(Object)} of
	 *         matching {@link IWorldEntity} is returned
	 */
	Optional<IWorldEntity> findWorldEntityByName(Locale locale, String name);

	/**
	 * <p>
	 * searches {@link IWorldEntity}s by their <strong>complete name in any
	 * language</strong>
	 * </p>
	 * 
	 * @param part
	 * @return result collection that might be empty if there are no matching
	 *         {@link IWorldEntity}s
	 */

	Collection<IWorldEntity> searchWorldEntityByName(String name);

	/**
	 * <p>
	 * searches {@link IWorldEntity}s by a <strong>part of their name in any
	 * language</strong>
	 * </p>
	 * 
	 * @param part
	 * @return result collection that might be empty if there are no matching
	 *         {@link IWorldEntity}s
	 */
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

	/**
	 * <p>
	 * save the given entity
	 * </p>
	 * 
	 * @param entity
	 * @return true if successful, else false
	 */
	boolean save(IWorldEntity entity);
}
