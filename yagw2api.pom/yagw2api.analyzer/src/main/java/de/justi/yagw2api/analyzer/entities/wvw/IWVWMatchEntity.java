package de.justi.yagw2api.analyzer.entities.wvw;

import java.util.Date;
import java.util.Map;

import com.google.common.base.Optional;

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
	
	Map<Date, IWVWScoresEmbeddable> getScores();
	Optional<IWVWScoresEmbeddable> getLatestScores();
	
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
	 * @param setupWorldReferences
	 */
	void synchronizeWithModel(Date timestamp, IWVWMatch model, boolean setupWorldReferences);
}
