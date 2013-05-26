package model.wvw;

import java.util.Set;

import com.google.common.base.Optional;

public interface IWVWModelFactory {
	// builders
	IWVWMap.IWVWMapBuilder createMapBuilder();
	IWVWObjective.IWVWObjectiveBuilder createObjectiveBuilder();
	IWVWMatch.IWVWMatchBuilder createMatchBuilder();
	
	// creation	
	IWVWScores createScores();
	
	// getting
	IWVWMapType getMapTypeForDTOString(String string);
	Set<IWVWLocationType> allLocationTypes();
	Optional<IWVWLocationType> getLocationTypeForObjectiveId(int objectiveId);
	Set<IWVWMapType> allMapTypes();
	IWVWMapType getCenterMapType();
	IWVWMapType getGreenMapType();
	IWVWMapType getRedMapType();
	IWVWMapType getBlueMapType();
}
