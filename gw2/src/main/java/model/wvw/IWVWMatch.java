package model.wvw;

import model.IWorld;

public interface IWVWMatch {
	String getId();
	IWorld[] getWorlds();
	IWorld getRedWOrld();
	IWorld getGreenWorld();
	IWorld getBlueWorld();
	IWVWMap getCenterMap();
	IWVWMap getBlueMap();
	IWVWMap getRedMap();
	IWVWMap getGreenMap();
	IWVWScores getScores();
}
