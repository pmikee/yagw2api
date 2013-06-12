package de.justi.yagw2api.analyzer.entities.wvw;

import java.util.Date;

import de.justi.yagw2api.analyzer.entities.IEntity;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;

public interface IWVWMatchEntity extends IEntity {
	String getOriginMatchId();

	Date getStartTimestamp();

	Date getEndTimestamp();

	IWorldEntity getRedWorld();

	IWorldEntity getGreenWorld();

	IWorldEntity getBlueWorld();

	/**
	 * <p>
	 * Synchronize this with the given {@link IWVWMatch}
	 * </p>
	 * <p>
	 * Synchronization is not persisted directly, therefore you have to use
	 * {@link IWVWMatchEntityDAO#save(IWVWMatchEntity)} to persist them.
	 * </p>
	 * 
	 * @param model
	 * @param setupWorldReferences TODO
	 * @return true if successfully, else false
	 */
	boolean synchronizeWithModel(IWVWMatch model, boolean setupWorldReferences);
}
