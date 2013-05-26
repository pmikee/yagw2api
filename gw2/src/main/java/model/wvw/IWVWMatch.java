package model.wvw;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import model.IWorld;
import api.dto.IWVWMatchDTO;

public interface IWVWMatch {
	interface IWVWMatchBuilder {
		IWVWMatch build();
		IWVWMatchBuilder fromMatchDTO(IWVWMatchDTO dto, Locale locale);
		IWVWMatchBuilder redScore(int score);
		IWVWMatchBuilder blueScore(int score);
		IWVWMatchBuilder greenScore(int score);
	}
	
	Set<IWorld> searchWorldsByNamePattern(Pattern searchPattern);
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
