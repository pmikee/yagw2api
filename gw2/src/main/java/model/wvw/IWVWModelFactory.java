package model.wvw;

import java.util.Set;

import model.IWorld;

import com.google.common.base.Optional;

public interface IWVWModelFactory {
	// builders
	IWVWMapBuilder createMapBuilder();
	IWVWObjectiveBuilder createObjectiveBuilder();
	IWVWMatchBuilder createMatchBuilder();
	
	// creation	
	IWVWMatch createWVWMatch(String id, IWorld redWorld, IWorld greenWorld, IWorld blueWorld, IWVWMap centerMap, IWVWMap redMap, IWVWMap greenMap, IWVWMap blueMap);
	IWVWScores createScores();
	IWVWObjective createObjective(IWVWLocationType location);
	
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
