package model.wvw;

import java.util.Map;
import java.util.Set;

import model.IHasChannel;
import model.wvw.types.IWVWLocationType;
import model.wvw.types.IWVWObjective;

import api.dto.IWVWMapDTO;

import com.google.common.base.Optional;

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
	Map<IWVWLocationType, IHasWVWLocation> getMappedByPosition();
	Set<IHasWVWLocation> getEverything();
	Set<IWVWObjective> getObjectives();
	Optional<IWVWObjective> getByObjectiveId(int id);
	Optional<IHasWVWLocation> getByLocation(IWVWLocationType location);
	IWVWScores getScores();
}