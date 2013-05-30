package de.justi.yagw2api.core.wrapper.model.wvw;

import java.util.Map;
import java.util.Set;



import com.google.common.base.Optional;

import de.justi.yagw2api.core.arenanet.dto.IWVWMapDTO;
import de.justi.yagw2api.core.wrapper.model.IHasChannel;
import de.justi.yagw2api.core.wrapper.model.wvw.types.IWVWLocationType;
import de.justi.yagw2api.core.wrapper.model.wvw.types.IWVWMapType;

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
	
	IWVWMap createImmutableReference();
	/**
	 * can only be called once
	 * @param map not null
	 */
	void connectWithMatch(IWVWMatch match);
}