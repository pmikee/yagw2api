package model.wvw;

import model.IWorld;

public interface IWVWMatch {
	String getId();
	IWorld[] getWorlds();
	IWorld getRedWOrld();
	IWorld getGreenWorld();
	IWorld getBlueWorld(); 
	IWorld getWorldByDTOOwnerString(String dtoOwnerString);
	IWVWMap getCenterMap();
	IWVWMap getBlueMap();
	IWVWMap getRedMap();
	IWVWMap getGreenMap();
	IWVWScores getScores();
}
