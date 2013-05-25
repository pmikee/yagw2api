package model.wvw;

import java.util.Set;

public interface IWVWModelFactory {
	IWVWMapBuilder createMapBuilder();
	
	Set<IWVWMapType> allMapTypes();
	IWVWMapType getCenterMapType();
	IWVWMapType getGreenMapType();
	IWVWMapType getRedMapType();
	IWVWMapType getBlueMapType();
		
}
