package de.justi.yagw2api.analyzer.entities.wvw;

import java.util.Date;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;

public interface IWVWMatchEntityDAO {
	Optional<IWVWMatchEntity> newMatchEntityOf(IWVWMatch match, boolean setupWorldReferences);
	boolean save(IWVWMatchEntity entity);
	
	Optional<IWVWMatchEntity> findWVWMatchEntity(Date start, Date end, String originMatchId);
	
	/**
	 * <p>
	 * returns the {@link IWVWMatchEntity} that belongs to the given {@link IWVWMatch}
	 * </p>
	 * <p>
	 * if there is no such {@link IWVWMatchEntity} a new will be created
	 * </p>
	 * 
	 * @param world
	 * @return
	 */
	IWVWMatchEntity findOrCreateWVWMatchEntityOf(IWVWMatch match);
}
