package model.wvw;

import java.util.Set;

import com.google.common.base.Optional;

public interface IWVWModelFactory {
	IWVWMapType createMapTypeFromDTOString(String string);
	
	IWVWMapBuilder createMapBuilder();
	IWVWObjectiveBuilder createObjectiveBuilder();
	
	Set<IWVWLocationType> allLocationTypes();
	Optional<IWVWLocationType> getLocationTypeForObjectiveId(int objectiveId);
	
	Set<IWVWMapType> allMapTypes();
	IWVWMapType getCenterMapType();
	IWVWMapType getGreenMapType();
	IWVWMapType getRedMapType();
	IWVWMapType getBlueMapType();
		
}
