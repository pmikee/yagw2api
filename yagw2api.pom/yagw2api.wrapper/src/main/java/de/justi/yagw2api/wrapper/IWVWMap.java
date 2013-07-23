package de.justi.yagw2api.wrapper;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.IWVWMapDTO;
import de.justi.yagwapi.common.IHasChannel;

public interface IWVWMap extends IHasChannel {
	static interface IWVWMapBuilder {
		IWVWMap build();

		IWVWMapBuilder type(IWVWMapType type);

		IWVWMapBuilder objective(IWVWObjective objective);

		IWVWMapBuilder fromDTO(IWVWMapDTO dto);

		IWVWMapBuilder match(IWVWMatch match);

		IWVWMapBuilder redScore(int score);

		IWVWMapBuilder blueScore(int score);

		IWVWMapBuilder greenScore(int score);
	}

	Optional<IWVWMatch> getMatch();

	IWVWMapType getType();

	Map<IWVWLocationType, IHasWVWLocation<?>> getMappedByPosition();

	Set<IHasWVWLocation<?>> getEverything();

	Set<IWVWObjective> getObjectives();

	Optional<IWVWObjective> getByObjectiveId(int id);

	Optional<IHasWVWLocation<?>> getByLocation(IWVWLocationType location);

	IWVWScores getScores();

	int calculateGreenTick();

	int calculateBlueTick();

	int calculateRedTick();

	IWVWMap createUnmodifiableReference();

	/**
	 * can only be called once
	 * 
	 * @param map
	 *            not null
	 */
	void connectWithMatch(IWVWMatch match);
}