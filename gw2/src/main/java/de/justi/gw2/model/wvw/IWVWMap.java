package de.justi.gw2.model.wvw;

import java.util.Map;
import java.util.Set;



import com.google.common.base.Optional;

import de.justi.gw2.api.dto.IWVWMapDTO;
import de.justi.gw2.model.IHasChannel;
import de.justi.gw2.model.wvw.types.IWVWLocationType;
import de.justi.gw2.model.wvw.types.IWVWObjective;

public interface IWVWMap extends IHasChannel {
	static interface IWVWMapBuilder {
		IWVWMap build();
		IWVWMapBuilder type(IWVWMapType type);
		IWVWMapBuilder objective(IWVWObjective objective);
		IWVWMapBuilder fromDTO(IWVWMapDTO dto);

		IWVWMapBuilder redScore(int score);
		IWVWMapBuilder blueScore(int score);
		IWVWMapBuilder greenScore(int score);
	}
	
	IWVWMapType getType();
	Map<IWVWLocationType, IHasWVWLocation<?>> getMappedByPosition();
	Set<IHasWVWLocation<?>> getEverything();
	Set<IWVWObjective> getObjectives();
	Optional<IWVWObjective> getByObjectiveId(int id);
	Optional<IHasWVWLocation<?>> getByLocation(IWVWLocationType location);
	IWVWScores getScores();
	
	IWVWMap createImmutableReference();
}