package model.wvw;

import java.util.Locale;

import api.dto.IWVWMatchDTO;
import model.IWorld;

public interface IWVWMatch {
	interface IWVWMatchBuilder {
		IWVWMatch build();
		IWVWMatchBuilder fromMatchDTO(IWVWMatchDTO dto, Locale locale);
		IWVWMatchBuilder redScore(int score);
		IWVWMatchBuilder blueScore(int score);
		IWVWMatchBuilder greenScore(int score);
	}
	
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
