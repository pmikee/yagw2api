package de.justi.yagw2api.analyzer;

import java.util.List;
import java.util.Locale;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.IWorld;
import de.justi.yagw2api.wrapper.IWorldLocationType;

public interface IWorldEntity extends IEntity {
	Optional<String> getName(Locale locale);

	Optional<String> getName();

	Optional<String> getNameDE();

	Optional<String> getNameEN();

	Optional<String> getNameES();

	Optional<String> getNameFR();

	Optional<Integer> getOriginWorldId();

	IWorldLocationType getLocation();

	Optional<Locale> getWorldLocale();

	boolean addParticipatedAsRedInMatch(IWVWMatchEntity match);

	boolean addParticipatedAsBlueInMatch(IWVWMatchEntity match);

	boolean addParticipatedAsGreenInMatch(IWVWMatchEntity match);

	Iterable<IWVWMatchEntity> getParticipatedInMatches();

	List<IWVWMatchEntity> getParticipatedInMatchesAsRedWorld();

	List<IWVWMatchEntity> getParticipatedInMatchesAsGreenWorld();

	List<IWVWMatchEntity> getParticipatedInMatchesAsBlueWorld();

	/**
	 * <p>
	 * Synchronize this with the given {@link IWorld}
	 * </p>
	 * <p>
	 * Synchronization is not persisted directly, therefore you have to use
	 * {@link IWorldEnityDAO#save(IWorldEntity)} to persist them.
	 * </p>
	 * 
	 * @param model
	 * @return true if successfully, else false
	 */
	boolean synchronizeWithModel(IWorld model);
}
