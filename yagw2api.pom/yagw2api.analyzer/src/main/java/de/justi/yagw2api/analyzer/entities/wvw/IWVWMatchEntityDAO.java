package de.justi.yagw2api.analyzer.entities.wvw;

import java.util.Date;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.IWVWMatch;

public interface IWVWMatchEntityDAO {
	Optional<IWVWMatchEntity> newMatchEntityOf(IWVWMatch match);

	boolean save(IWVWMatchEntity entity);

	Optional<IWVWMatchEntity> findWVWMatchEntity(Date start, Date end, String originMatchId);

	/**
	 * <p>
	 * returns the {@link IWVWMatchEntity} that belongs to the given
	 * {@link IWVWMatch}
	 * </p>
	 * <p>
	 * if there is no such {@link IWVWMatchEntity} a new will be created
	 * </p>
	 * 
	 * @param world
	 * @return
	 */
	IWVWMatchEntity findOrCreateWVWMatchEntityOf(IWVWMatch match);

	/**
	 * <p>
	 * Synchronize this with the given {@link IWVWMatch}
	 * </p>
	 * <p>
	 * Synchronization is not persisted directly, therefore you have to use
	 * {@link IWVWMatchEntityDAO#save(IWVWMatchEntity)} to persist them.
	 * </p>
	 * 
	 * @param entity
	 * @param timestamp
	 * @param model
	 */
	void synchronizeEntityWithModel(IWVWMatchEntity entity, Date timestamp, IWVWMatch model);
}
